// 会议室 II
// 给你一个会议时间安排的数组 intervals ，
// 每个会议时间包括开始和结束的时间 intervals[i] = [starti, endi] ，
// 返回所需会议室的最小数量。
// 测试链接 : https://leetcode.cn/problems/meeting-rooms-ii/

// 定义最大会议数量
#define MAX_N 10000

// 简单的输出函数，避免使用标准库
void print_result(int result) {
    // 由于不能使用标准库，我们通过返回值来表示结果
    // 在实际使用中，可以通过其他方式获取结果
}

// 时间复杂度: O(n*logn)
// 空间复杂度: O(n)
// 解题思路:
// 1. 将所有会议按照开始时间排序
// 2. 使用数组维护当前正在使用的会议室的结束时间
// 3. 遍历排序后的会议:
//    - 如果有会议室的结束时间小于等于当前会议的开始时间，说明有会议室空闲，可以复用
//    - 否则需要新的会议室
// 4. 会议室数量就是所需的最少会议室数量

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

// 查找最小结束时间的会议室索引
int findMinEndTimeRoom(int endTimes[], int size) {
    if (size <= 0) return -1;
    
    int minIndex = 0;
    for (int i = 1; i < size; i++) {
        if (endTimes[i] < endTimes[minIndex]) {
            minIndex = i;
        }
    }
    return minIndex;
}

// 主函数：计算最少需要的会议室数量
int minMeetingRooms(int intervals[][2], int n) {
    // 边界条件处理
    if (n <= 0) {
        return 0;
    }

    // 按照会议开始时间排序
    // 时间复杂度: O(n*logn)
    quickSort(intervals, 0, n - 1);

    // 使用数组维护当前正在使用的会议室的结束时间
    int endTimes[MAX_N];
    int roomCount = 0;

    // 遍历所有会议
    // 时间复杂度: O(n^2) （可以通过堆优化到O(n*logn)）
    for (int i = 0; i < n; i++) {
        int start = intervals[i][0];
        int end = intervals[i][1];

        // 查找是否有会议室空闲
        int freeRoomIndex = -1;
        for (int j = 0; j < roomCount; j++) {
            if (endTimes[j] <= start) {
                freeRoomIndex = j;
                break;
            }
        }

        // 如果有空闲会议室，复用它
        if (freeRoomIndex != -1) {
            endTimes[freeRoomIndex] = end;
        } else {
            // 否则需要新的会议室
            endTimes[roomCount] = end;
            roomCount++;
        }
    }

    // 返回所需的最少会议室数量
    return roomCount;
}

// 测试函数
int main() {
    // 测试用例1: [[0,30],[5,10],[15,20]]
    // 预期输出: 2
    int intervals1[3][2] = {{0, 30}, {5, 10}, {15, 20}};
    int result1 = minMeetingRooms(intervals1, 3);
    // printf("%d\n", result1); // 2

    // 测试用例2: [[7,10],[2,4]]
    // 预期输出: 1
    int intervals2[2][2] = {{7, 10}, {2, 4}};
    int result2 = minMeetingRooms(intervals2, 2);
    // printf("%d\n", result2); // 1

    // 测试用例3: [[9,10],[4,9],[4,17]]
    // 预期输出: 2
    int intervals3[3][2] = {{9, 10}, {4, 9}, {4, 17}};
    int result3 = minMeetingRooms(intervals3, 3);
    // printf("%d\n", result3); // 2

    // 测试用例4: []
    // 预期输出: 0
    int intervals4[0][2];
    int result4 = minMeetingRooms(intervals4, 0);
    // printf("%d\n", result4); // 0
    
    // 为了测试，我们添加一些输出
    print_result(result1); // 2
    print_result(result2); // 1
    print_result(result3); // 2
    print_result(result4); // 0

    return 0;
}