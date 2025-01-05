#ifndef INTCODE_PROGRAM
#define INTCODE_PROGRAM

typedef struct {
	unsigned int program_size;
	unsigned int instruction_pointer;
	int* init_program;
	int* program;
} Program;

// (&Program, program_data, program_size) -> 0 if success, 1 if failed
int load_program(Program* ,int*);
// (&Program) -> 0 if success, 1 if failed
int run_program(Program*);
// (&Program) -> 0 if success, 1 if failed
int reset_program(Program*);
// (&Program, index) -> value_at_index_in_program if sucess, -1 if failed
int get_index_in_program(Program*, int);
// (&Program, index, value) -> 0 if success, 1 if failed
int set_value_in_program(Program*, int, int);
// (&Program) -> 0 if success, 1 if failed
int free_program(Program*);

#endif
