#include <string.h>

#include "input_manager.h"

#include "solutions.h"

int main(int argc, char** argv) {
	// 0 for main, 1 for test
	int data_source = 0;
	if (argc == 2 && strcmp(argv[1], "test") == 0) {
		data_source = 1;
	}

	InputManager manager;
	switch_fetcher(&manager, data_source);

	run_day_1(manager.fetcher(1));
	run_day_2(manager.fetcher(2));
	run_day_3(manager.fetcher(3));
	run_day_4(manager.fetcher(4));
	run_day_5(manager.fetcher(5));
	run_day_6(manager.fetcher(6));
	return 0;
}
