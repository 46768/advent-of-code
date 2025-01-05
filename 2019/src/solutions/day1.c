#include "solutions.h"

#include <stdio.h>
#include <stdlib.h>

#include "logger.h"
#include "file_reader.h"

int* parse_input_day_1(FileData data, int* data_size) {
	int capacity = 1;
	int* ret_data = (int*)malloc(capacity*sizeof(int));
	int line_cnt = 0;

	char* line_ptr = NULL;
	size_t line_size;

	if (ret_data == NULL) {
		error("Failed to allocate return data");
		*data_size = 0;
		return ret_data;
	}

	if (data.file_pointer != NULL) {
		while (getline(&line_ptr, &line_size, data.file_pointer) != -1) {
			ret_data[line_cnt] = atoi(line_ptr);
			line_cnt++;
			if (line_cnt == capacity) {
				capacity *= 2;
				ret_data = realloc(ret_data, capacity*sizeof(int));
				if (ret_data == NULL) {
					error("Failed to reallocate return data");
					*data_size = 0;
					return ret_data;
				}
			}
		}
	} else {
		error("file is null");
		*data_size = 0;
		return ret_data;
	}

	*data_size = line_cnt;
	return ret_data;
};

void part_1_day_1(int* inpt, int inpt_size) {
	int fuel_requirement = 0;
	for (int i = 0; i < inpt_size; i++) {
		fuel_requirement += (inpt[i]/3)-2;
	}
	log("Fuel required: %d", fuel_requirement);
}

void part_2_day_1(int* inpt, int inpt_size) {
	int fuel_requirement = 0;
	for (int i = 0; i < inpt_size; i++) {
		int fuel_required = (inpt[i]/3)-2;
		while (fuel_required > 0) {
			fuel_requirement += fuel_required;
			fuel_required = (fuel_required/3)-2;
			if (fuel_required < 0) fuel_required = 0;
		}
	}
	log("Recursive Fuel required: %d", fuel_requirement);
}

void run_day_1(FileData inpt_data) {
	newline();
	int data_size = 0;
	int* inpt = parse_input_day_1(inpt_data, &data_size);
	part_1_day_1(inpt, data_size);
	part_2_day_1(inpt, data_size);
	free(inpt);
}
