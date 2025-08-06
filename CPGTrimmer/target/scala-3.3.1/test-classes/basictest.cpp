int main(void) {
	int x = 4; // watch for operator.assignment CPG that creates empty block node
	int y = 6;

	{} // basic empty block

	for (;;) { // creates empty block
		if (x == 4) { 
			break;
		}
	}

	if (x > y) {
		x = 44;
	}
	
	size_t s = sizeof(x);

	return x;
}
