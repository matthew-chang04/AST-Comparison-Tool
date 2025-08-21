#!/usr/bin/env bash
set -o errexit
set -o pipefail
set -o nounset
set -eu

readonly JOERN_VERSION="v4.0.407"

if [ "$(uname)" = 'Darwin' ]; then
  # get script location
  # https://unix.stackexchange.com/a/96238
  if [ "${BASH_SOURCE:-x}" != 'x' ]; then
    this_script=$BASH_SOURCE
  elif [ "${ZSH_VERSION:-x}" != 'x' ]; then
    setopt function_argzero
    this_script=$0
  elif eval '[[ -n ${.sh.file} ]]' 2>/dev/null; then
    eval 'this_script=${.sh.file}'
  else
    echo 1>&2 "Unsupported shell. Please use bash, ksh93 or zsh."
    exit 2
  fi
  relative_directory=$(dirname "$this_script")
  SCRIPT_ABS_DIR=$(cd "$relative_directory" && pwd)
else
  SCRIPT_ABS_PATH=$(readlink -f "$0")
  SCRIPT_ABS_DIR=$(dirname "$SCRIPT_ABS_PATH")
fi

# Check required tools are installed.
check_installed() {
  if ! type "$1" > /dev/null; then
    echo "Please ensure you have $1 installed."
    exit 1
  fi
}

readonly JOERN_INSTALL="$SCRIPT_ABS_DIR/joern-inst"
JOERN_CLI_DIR=""

echo "Examining Joern installation..."
if [ ! -d "${JOERN_INSTALL}" ]; then
    echo "Cannot find Joern installation at ${JOERN_INSTALL}"
    echo "Installing directly with wget..."
    
    check_installed "wget"
    check_installed "unzip"
    
    # Download joern-cli.zip directly with wget (more reliable for large files)
    echo "Downloading joern-cli.zip..."
    wget --continue --tries=3 --timeout=120 --read-timeout=60 \
         "https://github.com/joernio/joern/releases/download/$JOERN_VERSION/joern-cli.zip" \
         -O "$SCRIPT_ABS_DIR/joern-cli.zip"
    
    # Create installation directory
    mkdir -p "$JOERN_INSTALL"
    
    # Extract
    echo "Extracting Joern..."
    unzip -qo -d "$JOERN_INSTALL" "$SCRIPT_ABS_DIR/joern-cli.zip"
    
    # Clean up zip file
    rm "$SCRIPT_ABS_DIR/joern-cli.zip"
    
    # Check the actual directory structure and find the joern executables
    echo "Checking extracted directory structure..."
    ls -la "$JOERN_INSTALL"
    
    # Find where the joern executables actually are
    JOERN_CLI_DIR=$(find "$JOERN_INSTALL" -name "joern" -type f | head -1 | xargs dirname)
    echo "Found joern executables in: $JOERN_CLI_DIR"
    
    if [ -z "$JOERN_CLI_DIR" ]; then
        echo "Error: Could not find joern executable in extracted files"
        find "$JOERN_INSTALL" -type f -name "*joern*" | head -10
        exit 1
    fi
    
    # Create symlinks using the actual path
    echo "Creating symlinks..."
    pushd $SCRIPT_ABS_DIR
    ln -sf "$JOERN_CLI_DIR/joern" . || true
    ln -sf "$JOERN_CLI_DIR/joern-parse" . || true
    ln -sf "$JOERN_CLI_DIR/fuzzyc2cpg.sh" . || true
    ln -sf "$JOERN_CLI_DIR/joern-scan" . || true
    popd
    
    echo "Joern installation completed successfully!"
fi

# Set JOERN_CLI_DIR if not already set (for existing installations)
if [ -z "$JOERN_CLI_DIR" ]; then
    echo "Searching for joern executable..."
    echo "Contents of joern-inst:"
    find "$JOERN_INSTALL" -type f -name "*joern*" | head -10
    echo "All executable files:"
    find "$JOERN_INSTALL" -type f -executable | head -10
    echo "Directory structure:"
    find "$JOERN_INSTALL" -type d | head -10
    
    # Try different ways to find joern
    JOERN_CLI_DIR=$(find "$JOERN_INSTALL" -name "joern" -type f | head -1 | xargs dirname 2>/dev/null || echo "")
    
    if [ -z "$JOERN_CLI_DIR" ]; then
        # Try looking for joern script or binary in common locations
        for possible_dir in "$JOERN_INSTALL" "$JOERN_INSTALL/bin" "$JOERN_INSTALL/joern-cli"; do
            if [ -f "$possible_dir/joern" ]; then
                JOERN_CLI_DIR="$possible_dir"
                break
            fi
        done
    fi
    
    if [ -z "$JOERN_CLI_DIR" ]; then
        echo "Error: Could not find joern executable"
        echo "Please check the extracted contents above and manually locate the joern executable"
        exit 1
    else
        echo "Found joern executable in: $JOERN_CLI_DIR"
    fi
fi

# Build the plugin
echo "Compiling (sbt createDistribution)..."
pushd $SCRIPT_ABS_DIR
rm -f lib
sbt clean createDistribution
popd

# Install the plugin to Joern
echo "Installing plugin to Joern..."
if [ -n "$JOERN_CLI_DIR" ] && [ -d "$JOERN_CLI_DIR" ]; then
    pushd "$JOERN_CLI_DIR"
    ./joern --remove-plugin cpp-type-recovery || true  # Don't fail if plugin doesn't exist
    ./joern --add-plugin "$SCRIPT_ABS_DIR/cpp-type-recovery.zip"
    popd
    echo "Plugin installation completed!"
else
    echo "Error: Could not find Joern CLI directory for plugin installation"
    exit 1
fi
