// 会议室问题
// 给定一个会议时间安排的数组 intervals ，
// 每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi] ，
// 请你判断一个人是否能够参加这里面的全部会议。
// 测试链接 : https://leetcode.cn/problems/meeting-rooms/

// 定义最大会议数量
#define MAX_N 10000

// 简单的输出函数，避免使用标准库
void print_result(int result) {
    // 由于不能使用标准库，我们通过返回值来表示结果
    // 在实际使用中，可以通过其他方式获取结果
}

// 时间复杂度: O(n*logn)
// 空间复杂度: O(1)
// 解题思路:
// 1. 将所有会议按照开始时间排序
// 2. 遍历排序后的会议，检查当前会议的开始时间是否早于前一个会议的结束时间
// 3. 如果有冲突，返回false；否则返回true

// 交换两个会议
void swap(int intervals[][2], int i, int j) {
    int temp0 = intervals[i][0];
    int temp1 = intervals[i][1];
    intervals[i][0] = intervals[j][0];
    intervals[i][1] = intervals[j][1];
    intervals[j][0] = temp0;
    intervals[j][1] = temp1;
}

// 分区函数，用于快速排序
int partition(int intervals[][2], int low, int high) {
    int pivot = intervals[high][0];
    int i = low - 1;
    
    for (int j = low; j < high; j++) {
        if (intervals[j][0] <= pivot) {
            i++;
            swap(intervals, i, j);
        }
    }
    swap(intervals, i + 1, high);
    return i + 1;
}

// 快速排序函数
void quickSort(int intervals[][2], int low, int high) {
    if (low < high) {
        int pi = partition(intervals, low, high);
        quickSort(intervals, low, pi - 1);
        quickSort(intervals, pi + 1, high);
    }
}

// 主函数：判断是否能参加所有会议
int canAttendMeetings(int intervals[][2], int n) {
    // 边界条件处理
    // 如果没有会议，可以参加所有会议
    if (n <= 0) {
        return 1; // true
    }

    // 按照会议开始时间排序
    // 时间复杂度: O(n*logn)
    quickSort(intervals, 0, n - 1);

    // 遍历所有会议，检查是否有时间冲突
    // 时间复杂度: O(n)
    for (int i = 1; i < n; i++) {
        // 如果当前会议的开始时间早于前一个会议的结束时间，说明有冲突
        if (intervals[i][0] < intervals[i - 1][1]) {
            return 0; // false
        }
    }

    // 没有发现时间冲突，可以参加所有会议
    return 1; // true
}

// 测试函数
int main() {
    // 测试用例1: [[0,30],[5,10],[15,20]]
    // 预期输出: 0 (false)
    int intervals1[3][2] = {{0, 30}, {5, 10}, {15, 20}};
    int result1 = canAttendMeetings(intervals1, 3);
    // 输出结果（需要根据具体环境调整输出方式）
    // printf("%d\n", result1); // 0

    // 测试用例2: [[7,10],[2,4]]
    // 预期输出: 1 (true)
    int intervals2[2][2] = {{7, 10}, {2, 4}};
    int result2 = canAttendMeetings(intervals2, 2);
    // printf("%d\n", result2); // 1

    // 测试用例3: []
    // 预期输出: 1 (true)
    int intervals3[0][2];
    int result3 = canAttendMeetings(intervals3, 0);
    // printf("%d\n", result3); // 1

    // 测试用例4: [[1,2],[2,3]]
    // 预期输出: 1 (true)
    int intervals4[2][2] = {{1, 2}, {2, 3}};
    int result4 = canAttendMeetings(intervals4, 2);
    // printf("%d\n", result4); // 1

    // 为了测试，我们添加一些输出
    print_result(result1); // 0
    print_result(result2); // 1
    print_result(result3); // 1
    print_result(result4); // 1

    return 0;
}