#ifndef COM_LOGGER_H
#define COM_LOGGER_H

// Level - type
// 0 - INFO,
// 1 - WARN,
// 2 - ERROR,
// 3 - DEBUG

void _log(int, const char*, int, const char*, const char*, ...);

#define log(format, ...) _log(0, __FILE__, __LINE__, __func__, format, ##__VA_ARGS__)
#define warn(format, ...) _log(1, __FILE__, __LINE__, __func__, format, ##__VA_ARGS__)
#define error(format, ...) _log(2, __FILE__, __LINE__, __func__, format, ##__VA_ARGS__)
#define debug(format, ...) _log(3, __FILE__, __LINE__, __func__, format, ##__VA_ARGS__)

#endif
