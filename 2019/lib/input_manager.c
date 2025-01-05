#include "input_manager.h"

#include <stdio.h>

#include "file_reader.h"

FileData main_fetcher(int day) {
	char path_buf[24];
	sprintf(path_buf, "../data/main/day%d.txt", day);
	return get_file(path_buf);
}

FileData test_fetcher(int day) {
	char path_buf[24];
	sprintf(path_buf, "../data/test/day%d.txt", day);
	return get_file(path_buf);
}

void switch_fetcher(InputManager* manager, int fetcher_type) {
	if (fetcher_type == 0) {
		manager->fetcher = main_fetcher;
	} else {
		manager->fetcher = test_fetcher;
	}
}
