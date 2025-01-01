#include <stdio.h>
#include <string.h>

#include "inputManager.h"

#include "solutions.h"

int main(int argc, char** argv) {
	printf("Hello!\n");

	// 0 for main, 1 for test
	int data_source = 0;
	printf("argc: %d\n", argc);
	if (argc == 2 && strcmp(argv[1], "test") == 0) {
		data_source = 1;
	}

	InputManager manager;
	switch_fetcher(&manager, data_source);

	run_day_1(manager.fetcher(1));
	run_day_2(manager.fetcher(2));
	return 0;
}
