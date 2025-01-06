#include "hm_func.h"

#include <string.h>

unsigned int str_hash(const void* str) {
	const char* str_ptr = str;
	unsigned int hash = 0;
	while (*str_ptr) {
		hash = (hash * 31) + *str_ptr++;
	}
	return hash;
}

int str_cmp(const void* str_a, const void* str_b) {
	return strcmp((const char*)str_a, (const char*)str_b);
}
