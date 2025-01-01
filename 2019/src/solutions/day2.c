#include "solutions.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "logger.h"
#include "fileReader.h"

int* parse_input_day_2(FileData data, int* data_size) {
	int capacity = 1;
	int* ret_data = (int*)malloc(capacity*sizeof(int));
	int program_op_cnt = 0;

	char* line_ptr = NULL;
	size_t line_size;

	if (ret_data == NULL) {
		error("Failed to allocate return data");
		*data_size = 0;
		return ret_data;
	}

	if (data.file_pointer != NULL) {
		if (getline(&line_ptr, &line_size, data.file_pointer) != -1) {
			// Main processor
			int op_capacity = 1;
			int op_ptr = 0;
			char* op_buf = (char*)malloc(op_capacity*sizeof(char));
			for (int i = 0; i < line_size; i++) {
				if (line_ptr[i] == ',') {
					// Add string terminator
					op_buf[op_ptr] = '\0';

					// Parse opcode/erand
					ret_data[program_op_cnt] = atoi(op_buf);
					program_op_cnt++;
					op_ptr = 0;
					if (program_op_cnt >= capacity) {
						capacity *= 2;
						ret_data = realloc(ret_data, capacity*sizeof(int));
						if (ret_data == NULL) {
							error("Failed to reallocate return data");
							*data_size = 0;
							return ret_data;
						}
					}
				} else {
					op_buf[op_ptr] = line_ptr[i];
					op_ptr++;

					if (op_ptr >= op_capacity) {
						op_capacity *= 2;
						op_buf = realloc(op_buf, op_capacity*sizeof(int));
						if (op_buf == NULL) {
							error("Failed to reallocate opcode/ernad buffer");
							*data_size = 0;
							return ret_data;
						}
					}
				}
			}
			// Add string terminator
			op_buf[op_ptr] = '\0';

			// Parse opcode/erand
			ret_data[program_op_cnt] = atoi(op_buf);
			program_op_cnt++;
			op_ptr = 0;
			if (program_op_cnt >= capacity) {
				capacity *= 2;
				ret_data = realloc(ret_data, capacity*sizeof(int));
				if (ret_data == NULL) {
					error("Failed to reallocate return data");
					*data_size = 0;
					return ret_data;
				}
			}
			free(op_buf);
		} else {
			warn("Data is empty");
		}
		free(line_ptr);
	} else {
		error("file is null");
		*data_size = 0;
		return ret_data;
	}

	*data_size = program_op_cnt;
	return ret_data;
};

void part_1_day_2(int* inpt, int inpt_size) {
	int* program = (int*)malloc(inpt_size*sizeof(int));
	int program_ptr = 0;
	memcpy(program, inpt, inpt_size*sizeof(int));

	// As per problem, replace [1] with 12 and [2] with 2
	program[1] = 12;
	program[2] = 2;

	for (;program_ptr < inpt_size;) {
		int opcode = program[program_ptr];
		if (opcode == 99) break;
		int src1 = program[program_ptr+1];
		int src2 = program[program_ptr+2];
		int target = program[program_ptr+3];

		switch (opcode) {
			case 1:
				program[target] = program[src1] + program[src2];
				break;
			case 2:
				program[target] = program[src1] * program[src2];
				break;
			case 99:
				program_ptr = inpt_size; // Break the loop immediately
				break;
		}

		program_ptr += 4;
	}

	log("position 0: %d", program[0]);
	free(program);
}

void part_2_day_2(int* inpt, int inpt_size) {
	int* program = (int*)malloc(inpt_size*sizeof(int));

	for (int noun = 0; noun < 100; noun++) {
		for (int verb = 0 ;verb < 100; verb++) {
			int program_ptr = 0;
			memcpy(program, inpt, inpt_size*sizeof(int));

			// As per problem, replace [1] with noun and [2] with verb
			program[1] = noun;
			program[2] = verb;

			for (;program_ptr < inpt_size;) {
				int opcode = program[program_ptr];
				if (opcode == 99) break;
				int src1 = program[program_ptr+1];
				int src2 = program[program_ptr+2];
				int target = program[program_ptr+3];

				switch (opcode) {
					case 1:
						program[target] = program[src1] + program[src2];
						break;
					case 2:
						program[target] = program[src1] * program[src2];
						break;
					case 99:
						program_ptr = inpt_size; // Break the loop immediately
						break;
				}

				program_ptr += 4;
			}
			
			// 19690720 is the problem's magic number
			if (program[0] == 19690720) {
				log("100*noun + verb: %d", (100*noun)+verb);
				free(program);
				return;
			}
		}
	}
	log("End of d2p2");
}

void run_day_2(FileData inpt_data) {
	int data_size = 0;
	int* inpt = parse_input_day_2(inpt_data, &data_size);
	part_1_day_2(inpt, data_size);
	part_2_day_2(inpt, data_size);
	free(inpt);
}
