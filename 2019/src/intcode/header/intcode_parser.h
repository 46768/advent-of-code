#ifndef INTCODE_PARSER
#define INTCODE_PARSER

#include <stdlib.h>

// (intcode_string) -> [program_size, ...program_data] if success, NULL if failed
int* parse_intcode_string(char*, size_t);

#endif
