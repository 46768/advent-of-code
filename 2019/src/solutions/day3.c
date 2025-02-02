#include "solutions.h"

#include <stdlib.h>

#include "logger.h"
#include "file_reader.h"
#include "allocator.h"
#include "file_util.h"

typedef struct {
	int x;
	int y;
} Point;

typedef struct {
	Point start;
	Point end;
	int arr_size;
} Vector;

Vector* parse_input_day_3(FileData data) {
	if (data.file_pointer != NULL) {
		size_t section_capacity = 1;
		char* section_buf = (char*)allocate(section_capacity*sizeof(char));

		size_t capacity = 3;
		Vector* vectors = (Vector*)allocate(capacity*sizeof(Vector));
		int vector_ptr = 2; // data indexer
		int wire_size = 0;
		int vec_size_ptr = 0; // size indexer

		int section_status;
		Point current_point;
		current_point.x = 0;
		current_point.y = 0;
		while ((section_status = get_section(
						data.file_pointer,
						&section_buf,
						&section_capacity,
						',')) != -1) {
			Point next_point = current_point;

			// next point manipulation
			char direction = section_buf[0];
			int magnitude = atoi(&section_buf[1]);

			if (direction == 'U') {
				next_point.y += magnitude;
			} else if (direction == 'D') {
				next_point.y -= magnitude;
			} else if (direction == 'L') {
				next_point.x -= magnitude;
			} else { // Implied direction == 'R'
				next_point.x += magnitude;
			}

			Vector vec;
			vec.start = current_point;
			vec.end = next_point;

			current_point = next_point;

			vectors[vector_ptr++] = vec;
			wire_size++;

			if (vector_ptr >= capacity) {
				capacity *= 2;
				vectors = (Vector*)reallocate(vectors, capacity*sizeof(Vector));
			}

			if (section_status == 1) {
				current_point.x = 0;
				current_point.y = 0;

				Vector wire_s;
				wire_s.arr_size = wire_size;
				vectors[vec_size_ptr++] = wire_s;
				wire_size = 0;
			}
		}

		deallocate(section_buf);
		return vectors;
	};

	error("File is null");
	return NULL;
};

int is_inline(int bound_1, int bound_2, int x) {
	if (bound_1 < x && x < bound_2) return 1;
	if (bound_2 < x && x < bound_1) return 1;
	return 0;
}

int does_wire_intersect(Vector w1, Vector w2) {
	if (is_inline(w1.start.x, w1.end.x, w2.start.x) &&
		is_inline(w2.start.y, w2.end.y, w1.start.y)) return 1;
	if (is_inline(w2.start.x, w2.end.x, w1.start.x) &&
		is_inline(w1.start.y, w1.end.y, w2.start.y)) return 1;
	return 0;
}

void part_1_day_3(Vector* inpt) {
	Vector* vec_ptr = inpt+2;
	int wire_1_size = inpt[0].arr_size;
	int wire_2_size = inpt[1].arr_size;

	/*
	 * The logic here is that is there a point in
	 * first vector thats between the second vector bound
	 * and a point in the second vector thats between the
	 * first vector bound, if yes then intersection point is
	 * (v2.x, v1.y)
	 */

	int closest_distance = 999999;

	for (int i = 0; i < wire_1_size; i++) {
		Vector wire_1 = vec_ptr[i];
		for (int j = wire_1_size; j < wire_1_size+wire_2_size; j++) {
			Vector wire_2 = vec_ptr[j];
			int x_1_diff = wire_1.start.x - wire_1.end.x;

			if (!does_wire_intersect(wire_1, wire_2)) continue;

			int intersect_x = x_1_diff == 0 ? wire_1.start.x : wire_2.start.x;
			int intersect_y = x_1_diff == 0 ? wire_2.start.y : wire_1.start.y;
			int intersection_distance = abs(intersect_x)+abs(intersect_y);

			if (intersection_distance < closest_distance) {
				closest_distance = intersection_distance;
			}
		}
	}

	info("Distance of closest interesection point: %d", closest_distance);
}

int get_vector_step(Vector v) {
	int dx = abs(v.start.x - v.end.x);
	int dy = abs(v.start.y - v.end.y);
	return dx+dy;
}

void part_2_day_3(Vector* inpt) {
	Vector* vec_ptr = inpt+2;
	int wire_1_size = inpt[0].arr_size;
	int wire_2_size = inpt[1].arr_size;

	/*
	 * The logic here is that is there a point in
	 * first vector thats between the second vector bound
	 * and a point in the second vector thats between the
	 * first vector bound, if yes then intersection point is
	 * (v2.x, v1.y), find the first one, steps of each wire can be accmulated
	 * in the loop, then when an intersection is found, calculate the steps
	 * with the accmulated steps
	 */

	int smallest_steps = 1<<30;

	int w1_steps = 0;
	for (int i = 0; i < wire_1_size; i++) {
		Vector wire_1 = vec_ptr[i];
		int w2_steps = 0;
		for (int j = wire_1_size; j < wire_1_size+wire_2_size; j++) {
			Vector wire_2 = vec_ptr[j];

			if (does_wire_intersect(wire_1, wire_2)) {
				int x_1_diff = wire_1.start.x - wire_1.end.x;
				int intersect_x = x_1_diff == 0 ? wire_1.start.x : wire_2.start.x;
				int intersect_y = x_1_diff == 0 ? wire_2.start.y : wire_1.start.y;

				int final_w1_steps = w1_steps + abs(wire_1.start.x - intersect_x) +
					abs(wire_1.start.y - intersect_y);
				int final_w2_steps = w2_steps + abs(wire_2.start.x - intersect_x) +
					abs(wire_2.start.y - intersect_y);
				int final_steps = final_w1_steps+final_w2_steps;

				if (final_steps < smallest_steps) smallest_steps = final_steps;
			} else {
				w2_steps += get_vector_step(wire_2);
			}
		}
		w1_steps += get_vector_step(wire_1);
	}
	info("Smallest step took to get to an intersection: %d", smallest_steps);
}

void run_day_3(FileData inpt_data) {
	newline();
	Vector* inpt = parse_input_day_3(inpt_data);
	part_1_day_3(inpt);
	part_2_day_3(inpt);
	deallocate(inpt);
}
