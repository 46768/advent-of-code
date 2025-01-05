#ifndef INTCODE_CPU
#define INTCODE_CPU

#include "intcode_program.h"

// (&Program) -> 0 if success, 1 if failed, -1 if halted
int step_program(Program*);

#endif
