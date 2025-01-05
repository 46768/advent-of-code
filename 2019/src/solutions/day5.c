#include "solutions.h"

#include "logger.h"
#include "allocator.h"
#include "file_reader.h"
#include "intcode_program.h"
#include "intcode_parser.h"

int* parse_data_day_5(FileData inpt) {
	if (inpt.file_pointer != NULL) {
		return parse_intcode_file(inpt);
	};
	error("File is null");
	return NULL;
}

void part_1_day_5(int* program_data) {
	Program program;
	load_program(&program, program_data);

	input_program(&program, 1);

	run_program(&program);

	flush_stdout(&program);
	log("vmstdout flushed");
}
void part_2_day_5(int* program_data) {
	Program program;
	load_program(&program, program_data);

	input_program(&program, 5);

	run_program(&program);
	newline();

	flush_stdout(&program);
	log("vmstdout flushed");
}

void run_day_5(FileData inpt) {
	newline();
	int* program_data = parse_data_day_5(inpt);
	part_1_day_5(program_data);
	part_2_day_5(program_data);
	deallocate(program_data);
}
