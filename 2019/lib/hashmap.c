#include "hashmap.h"

#include <stdlib.h>

/*
typedef struct Node {
	void* key;
	void* value;
	struct Node* next;
} Node;

typedef struct {
	Node** buckets;
	int size;
	size_t k_size;
	size_t v_size;
	unsigned int (*hash_func)(const void* key);
	int (*key_cmp)(const void* a, const void* b);
} HashMap;
 */

HashMap* hm_new(
		int size,
		size_t k_size,
		size_t v_size,
		unsigned int (*hash_fn)(const void* k),
		int (*cmp_fn)(const void* a, const void* b)
) {
	HashMap* hm = malloc(sizeof(HashMap));
	hm->buckets = calloc(size, sizeof(Node*));
	hm->size = size;
	hm->k_size = k_size;
	hm->v_size = v_size;
	hm->hash_func = hash_fn;
	hm->key_cmp = cmp_fn;

	return hm;
}
