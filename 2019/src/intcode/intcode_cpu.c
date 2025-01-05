#include "header/intcode_cpu.h"

#include "logger.h"

typedef Program Program;

// 1,a,b,c -> *c = (*a)+(*b)
// 2,a,b,c -> *c = (*a)*(*b)
// 99 -> HALT

int step_program(Program* program) {
	int* program_state = program->program;
	int instruction_pointer = program->instruction_pointer;

	int opcode = program_state[instruction_pointer];
	if (opcode == 99) return -1;

	int a = program_state[instruction_pointer+1],
	b = program_state[instruction_pointer+2],
	c = program_state[instruction_pointer+3];
	
	int step_size = 0;
	switch (opcode) {
		case 1:
			program_state[c] = program_state[a]+program_state[b];
			step_size = 4;
			break;
		case 2:
			program_state[c] = program_state[a]*program_state[b];
			step_size = 4;
			break;
		default:
			error("Unknown opcode: %d: at index: %d", opcode, instruction_pointer);
			step_size = 0;
			return 1;
	}
	program->instruction_pointer += step_size;

	return 0;
}
