#ifndef COM_HASHSET_H
#define COM_HASHSET_H

#include <stdlib.h>

typedef struct Node_HS {
	void* value;
	struct Node_HS* next;
} Node_HS;

typedef struct {
	Node_HS** buckets;
	int size;
	size_t v_size;
	unsigned int (*hash_func)(const void* val);
	int (*val_cmp)(const void* a, const void* b);
} HashSet;

HashSet* hs_new(
		int,
		size_t,
		unsigned int (*)(const void*),
		int (*)(const void*, const void*)
);

void hs_insert(HashSet*, const void*);
int hs_exists(HashSet*, const void*);
void hs_free(HashSet*);

#endif
