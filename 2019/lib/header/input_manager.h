#ifndef COM_INPUT_MANAGER_H
#define COM_INPUT_MANAGER_H

#include "file_reader.h"

typedef FileData (*data_fetcher)(int);
typedef struct {
	data_fetcher fetcher;
} InputManager;

FileData main_fetcher(int);
FileData test_fetcher(int);
void switch_fetcher(InputManager*, int);

#endif
