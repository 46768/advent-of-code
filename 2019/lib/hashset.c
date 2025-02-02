#include "hashset.h"

#include <stdlib.h>
#include <string.h>

#include "logger.h"
#include "allocator.h"
#include "booltype.h"

HashSet* hs_new(
		int size,
		size_t v_size,
		unsigned int (*hash_fn)(const void*),
		int (*cmp_fn)(const void*, const void*)
) {
	HashSet* hs = allocate(sizeof(HashSet));
	hs->buckets = calloc(size, sizeof(Node_HS*));
	hs->size = size;
	hs->v_size = v_size;
	hs->hash_func = hash_fn;
	hs->val_cmp = cmp_fn;
	hs->hash_func("B");
	debug("hs made");

	return hs;
}

void hs_insert(HashSet* hs, const void* v) {
	debug("hs insert");
	debug("v: %s", (char*)(v));
	debug("is nullptr: %d", hs == NULL ? 1 : 0);
	debug("hs size: %d", hs->size);
	unsigned int hash_idx = hs->hash_func(v) % hs->size;
	debug("got hash");

	Node_HS* node = hs->buckets[hash_idx];
	debug("got node");
	while (node) {
		if (hs->val_cmp(node->value, v) == 0) {
			debug("node found");
			return;
		}
		debug("checked node");
		node = node->next;
		debug("got node");
	}

	node = allocate(sizeof(Node_HS));
	node->value = allocate(hs->v_size);
	memcpy(node->value, v, hs->v_size);
	node->next = hs->buckets[hash_idx];
	hs->buckets[hash_idx] = node;
	debug("node v: %c", *(char*)(node->value));
}

int hs_exists(HashSet* hs, const void* v) {
	unsigned int k_hash = hs->hash_func(v) % hs->size;
	Node_HS* node = hs->buckets[k_hash];
	while (node) {
		if (hs->val_cmp(node->value, v) == 0) {
			return TRUE;
		}
		node = node->next;
	}

	return FALSE;
}

void hs_free(HashSet* hs) {
	for (int i = 0; i < hs->size; i++) {
		Node_HS* node = hs->buckets[i];
		while (node) {
			Node_HS* temp = node;
			node = node->next;
			deallocate(temp->value);
			deallocate(temp);
		}
	}
	deallocate(hs->buckets);
	deallocate(hs);
}
