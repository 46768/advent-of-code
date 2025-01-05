#ifndef INTCODE_PARSER
#define INTCODE_PARSER

#include "file_reader.h"

// (intcode_string) -> [program_size, ...program_data] if success, NULL if failed
int* parse_intcode_file(FileData);

#endif
