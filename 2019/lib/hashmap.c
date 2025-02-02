#include "hashmap.h"

#include <stdlib.h>
#include <string.h>

#include "logger.h"
#include "allocator.h"
#include "booltype.h"

#include "hashset.h"

/*
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
 */

HashMap* hm_new(
		int size,
		size_t k_size,
		size_t v_size,
		unsigned int (*hash_fn)(const void*),
		int (*cmp_fn)(const void*, const void*)
) {
	HashMap* hm = allocate(sizeof(HashMap));
	hm->buckets = calloc(size, sizeof(Node_HM*));
	hm->size = size;
	hm->k_size = k_size;
	hm->v_size = v_size;
	hm->hash_func = hash_fn;
	hm->key_cmp = cmp_fn;

	return hm;
}

void hm_insert(HashMap* hm, const void* k, const void* v) {
	unsigned int hash_idx = hm->hash_func(k) % hm->size;

	Node_HM* node = hm->buckets[hash_idx];
	while (node) {
		if (hm->key_cmp(node->key, k) == 0) {
			memcpy(node->value, v, hm->v_size);
			return;
		}
		node = node->next;
	}


	node = allocate(sizeof(Node_HM));
	node->key = allocate(hm->k_size);
	node->value = allocate(hm->v_size);
	memcpy(node->key, k, hm->k_size);
	memcpy(node->value, v, hm->v_size);
	node->next = hm->buckets[hash_idx];
	hm->buckets[hash_idx] = node;
	debug("Checking val for null");
	debug("is (arg)val NULL: %d", v == NULL ? 1 : 0);
	debug("is val NULL: %b", node->value == NULL);
	debug("%d", (*(HashSet**)(node->value))->size);
}

void* hm_get(HashMap* hm, const void* k) {
	debug("hm get");
	unsigned int hash_idx = hm->hash_func(k) % hm->size;
	debug("got hash");
	
	Node_HM* node = hm->buckets[hash_idx];
	debug("got node");
	while (node) {
		if (hm->key_cmp(node->key, k) == 0) {
			debug("node found");
			return node->value;
		}
		debug("checked node");
		node = node->next;
		debug("got node");
	}
	debug("node not found");

	return NULL;
}

int hm_k_exists(HashMap* hm, const void* k) {
	unsigned int k_hash = hm->hash_func(k) % hm->size;
	Node_HM* node = hm->buckets[k_hash];
	while (node) {
		if (hm->key_cmp(node->key, k) == 0) {
			return TRUE;
		}
		node = node->next;
	}

	return FALSE;
}

void hm_free(HashMap* hm) {
	for (int i = 0; i < hm->size; i++) {
		Node_HM* node = hm->buckets[i];
		while (node) {
			Node_HM* temp = node;
			node = node->next;
			deallocate(temp->key);
			deallocate(temp->value);
			deallocate(temp);
		}
	}
	deallocate(hm->buckets);
	deallocate(hm);
}
