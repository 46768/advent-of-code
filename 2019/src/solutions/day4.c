#include "solutions.h"

#include "logger.h"
#include "file_reader.h"
#include "file_util.h"
#include "allocator.h"

void parse_input_day_4(FileData inpt, int arr[2]) {
	size_t bound_size = 7;
	char* bound = (char*)allocate(bound_size);
	int idx = 0;
	while (get_section(inpt.file_pointer, &bound, &bound_size, '-') != -1) {
		arr[idx++] = atoi(bound);	
	}
	deallocate(bound);
}

/*
 * Valid password criteria
 * must be 6 digits long (handled by the bound)
 * must have a double digit
 * digits must always increases eg. 122334, not 122543
 */

int is_valid_passwd(int passwd) {
	int prev_lsd = 10;
	int double_digits = 0;
	while (passwd > 0) {
		int cur_lsd = passwd % 10;
		if (cur_lsd == prev_lsd) double_digits = 1;
		if (cur_lsd > prev_lsd) return 0;

		prev_lsd = cur_lsd;
		passwd /= 10;
	}
	return double_digits;
}

void part_1_day_4(int bound[2]) {
	int valid_passwd = 0;
	for (int i = bound[0]; i <= bound[1]; i++) {
		valid_passwd += is_valid_passwd(i);
	}

	log("Valid password count: %d", valid_passwd);
}

int is_valid_passwd_extended(int passwd) {
	int prev_lsd = 10;
	int double_digits = 0;
	int group_size = 1;
	while (passwd > 0) {
		int cur_lsd = passwd % 10;
		// check if previous digit is the same as current
		if (cur_lsd == prev_lsd) {
			// if the same then increase the group size
			group_size++;
		} else {
			// else if group size is 2 then theres a double digit
			if (group_size == 2) double_digits = 1;
			// reset group size
			group_size = 1;
		}
		// if current digit is larger than previous then it breaks
		// the rule
		if (cur_lsd > prev_lsd) return 0;

		prev_lsd = cur_lsd;
		passwd /= 10;
	}
	return double_digits;
}

void part_2_day_4(int bound[2]) {
	int valid_passwd = 0;
	for (int i = bound[0]; i <= bound[1]; i++) {
		valid_passwd += is_valid_passwd_extended(i);
	}

	log("Valid password count with extended rules: %d", valid_passwd);
}

void run_day_4(FileData inpt) {
	newline();
	int bound[2];
	parse_input_day_4(inpt, bound);
	part_1_day_4(bound);
	part_2_day_4(bound);
}
