#include "solutions.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "intcode_program.h"
#include "intcode_parser.h"

#include "logger.h"
#include "file_reader.h"

int* parse_input_day_2(FileData data) {
	if (data.file_pointer != NULL) {
		return parse_intcode_file(data);
	};
	error("File is null");
	return NULL;
}

void part_1_day_2(int* inpt) {
	Program program;
	load_program(&program, inpt);

	// As per problem, replace [1] with 12 and [2] with 2
	set_value_in_program(&program, 1, 12);
	set_value_in_program(&program, 2, 2);

	run_program(&program);

	log("position 0: %d", get_index_in_program(&program, 0));
	free_program(&program);
}

void part_2_day_2(int* inpt) {
	Program program;
	load_program(&program, inpt);

	for (int noun = 0; noun < 100; noun++) {
		for (int verb = 0 ;verb < 100; verb++) {
			reset_program(&program);

			// As per problem, replace [1] with noun and [2] with verb
			set_value_in_program(&program, 1, noun);
			set_value_in_program(&program, 2, verb);

			run_program(&program);
			
			// 19690720 is the problem's magic number
			if (get_index_in_program(&program, 0) == 19690720) {
				log("100*noun + verb: %d", (100*noun)+verb);
				free_program(&program);
				return;
			}
		}
	}
	free_program(&program);
}

void run_day_2(FileData inpt_data) {
	newline();
	int* inpt = parse_input_day_2(inpt_data);
	part_1_day_2(inpt);
	part_2_day_2(inpt);
	free(inpt);
}
