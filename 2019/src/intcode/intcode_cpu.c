#include "header/intcode_cpu.h"

#include "logger.h"

typedef Program Program;

// 1,a,b,c -> *c = (*a)+(*b)
// 2,a,b,c -> *c = (*a)*(*b)
// 3,a -> *a << stdin
// 4,a -> stdout << *a
// 99 -> HALT

int get_stdin(Program* program) {
	if (program->stdin_ptr >= INTCODE_STD_SIZE-1) return -1;
	return program->vmstdin[program->stdin_ptr++];
}

int print_stdout(Program* program, int val) {
	if (program->stdin_ptr >= INTCODE_STD_SIZE-1) return 0;
	program->vmstdout[program->stdout_ptr++] = val;
	return 1;
}

int parse_instruction(Program* program, int opcode, int a, int b, int c) {
	int* program_state = program->program;

	int ir1 = a;
	int ir2 = b;
	int ir3 = c;

	int* a_ptr = &program_state[a];
	int* b_ptr = &program_state[b];
	int* c_ptr = &program_state[c];
	
	int inst_opcode = opcode % 100;
	if ((opcode/100)%10) a_ptr = &ir1;
	if ((opcode/1000)%10) b_ptr = &ir2;
	if ((opcode/10000)%10) c_ptr = &ir3;

	int step_size = 0;
	switch (inst_opcode) {
		case 1:
			*c_ptr = (*a_ptr)+(*b_ptr);
			step_size = 4;
			break;
		case 2:
			*c_ptr = (*a_ptr)*(*b_ptr);
			step_size = 4;
			break;
		case 3:
			*a_ptr = get_stdin(program);
			step_size = 2;
			break;
		case 4:
			print_stdout(program, *a_ptr);
			step_size = 2;
			break;
		case 5:
			if (*a_ptr != 0) {
				program->instruction_pointer = *b_ptr;
				step_size = 0;
			} else {
				step_size = 3;
			}
			break;
		case 6:
			if (*a_ptr == 0) {
				program->instruction_pointer = *b_ptr;
				step_size = 0;
			} else {
				step_size = 3;
			}
			break;
		case 7:
			*c_ptr = *a_ptr < *b_ptr ? 1 : 0;
			step_size = 4;
			break;
		case 8:
			*c_ptr = *a_ptr == *b_ptr ? 1 : 0;
			step_size = 4;
			break;
		default:
			error("Unknown opcode: %d: at index: %d", inst_opcode, program->instruction_pointer);
			step_size = 0;
			return 0;
	}
	program->instruction_pointer += step_size;

	return 1;
}

int step_program(Program* program) {
	int* program_state = program->program;
	int instruction_pointer = program->instruction_pointer;

	int opcode = program_state[instruction_pointer];
	if (opcode == 99) return -1;

	int a = program_state[instruction_pointer+1],
	b = program_state[instruction_pointer+2],
	c = program_state[instruction_pointer+3];
	
	parse_instruction(program, opcode, a, b, c);

	return 1;
}
