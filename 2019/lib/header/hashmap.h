#ifndef COM_HASHMAP_H
#define COM_HASHMAP_H

#include <stdlib.h>

typedef struct Node_HM {
	void* key;
	void* value;
	struct Node_HM* next;
} Node_HM;

typedef struct {
	Node_HM** buckets;
	int size;
	size_t k_size;
	size_t v_size;
	unsigned int (*hash_func)(const void* key);
	int (*key_cmp)(const void* a, const void* b);
} HashMap;

HashMap* hm_new(
		int,
		size_t,
		size_t,
		unsigned int (*)(const void*),
		int (*)(const void*, const void*)
);

void hm_insert(HashMap*, const void*, const void*);
void* hm_get(HashMap*, const void*);
int hm_k_exists(HashMap*, const void*);
void hm_free(HashMap*);

#endif
