#include "solutions.h"

#include <stdio.h>

#include "logger.h"
#include "hashmap.h"
#include "hashset.h"
#include "hm_func.h"
#include "file_util.h"
#include "allocator.h"

HashMap* parse_data_day_6(FileData inpt) {
	debug("start parse");
	HashMap* orbit_hm = hm_new(
			1024,
			sizeof(char*),
			sizeof(HashSet**),
			str_hash,
			str_cmp
			);
	debug("created hm");
	char** hm_kv = (char**)allocate(2*sizeof(char*));
	debug("create km_kv");

	size_t section_cap = 8;
	char* section_buf = (char*)allocate(section_cap*sizeof(char));
	int get_res = 0;
	debug("section cap, buf");
	while ((get_res = get_section(inpt.file_pointer, &section_buf, &section_cap, ')')) != -1) {
		info("res: %d", get_res);
		hm_kv[get_res] = section_buf;
		if (get_res == 1) {
			info("hmkv: %s, %s", hm_kv[0], hm_kv[1]);
			if (!hm_k_exists(orbit_hm, hm_kv[0])) {
				debug("hm key not exist");
				HashSet* orbiter_hs = hs_new(
							256,
							sizeof(char*),
							str_hash,
							str_cmp);
				debug("made hs");
				debug("hs size: %d", orbiter_hs->size);
				hm_insert(orbit_hm, hm_kv[0], &orbiter_hs);
				debug("key inserted");
			}
			hs_insert(*(HashSet**)hm_get(orbit_hm, hm_kv[0]), hm_kv[1]);
			debug("val inserted");
		}
	}

	return orbit_hm;
}

void part_1_day_6(HashMap* orbit_map) {
	debug("start p1d6");
	HashSet* comSet = *(HashSet**)hm_get(orbit_map, "COM");
	debug("got comSet");
	info("%s", (char*)comSet->buckets[0]);
}

void part_2_day_6(HashMap* orbit_map) {
}

void run_day_6(FileData inpt) {
	/*
	HashMap* test_hm = hm_new(
			1024,
			sizeof(char*),
			sizeof(char*),
			str_hash,
			str_cmp
			);
	hm_insert(test_hm, "Hello", "Hi");
	hm_insert(test_hm, "Helloe", "Ho");
	debug("%s", hm_get(test_hm, "Hello"));
	debug("%s", hm_get(test_hm, "Helloe"));
	hm_insert(test_hm, "Hello", "He");
	debug("%s", hm_get(test_hm, "Hello"));
	*/
	HashMap* orbit_hm = parse_data_day_6(inpt);
	part_1_day_6(orbit_hm);
	part_2_day_6(orbit_hm);
}
