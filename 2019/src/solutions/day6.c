#include "solutions.h"

#include <stdio.h>

#include "hashmap.h"
#include "hm_func.h"
#include "file_util.h"
#include "allocator.h"

HashMap* parse_data_day_6(FileData inpt) {
	HashMap* orbit_hm = hm_new(
			1024,
			sizeof(char*),
			sizeof(char*),
			str_hash,
			str_cmp
			);
	char** hm_kv = (char**)allocate(2*sizeof(char*));

	size_t section_cap = 8;
	char* section_buf = (char*)allocate(section_cap*sizeof(char));
	int get_res = 0;
	while ((get_res = get_section(inpt.file_pointer, &section_buf, &section_cap, ')')) != -1) {
		hm_kv[get_res] = section_buf;
		if (get_res == 1) {
			hm_insert(orbit_hm, hm_kv[0], hm_kv[1]);
		}
	}

	return orbit_hm;
}

void part_1_day_6(HashMap* orbit_map) {
}

void part_2_day_6(HashMap* orbit_map) {
}

void run_day_6(FileData inpt) {

}
