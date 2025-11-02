/**
 * 二分查找算法实现 (C++版本)
 * 
 * 核心思想：
 * 1. 在有序数组中查找特定元素
 * 2. 每次比较中间元素，根据比较结果缩小搜索范围
 * 3. 时间复杂度：O(log n)，空间复杂度：O(1)
 * 
 * 应用场景：
 * 1. 在有序数组中查找元素
 * 2. 查找插入位置
 * 3. 查找边界值
 * 
 * 工程化考量：
 * 1. 边界条件处理（空数组、单元素数组）
 * 2. 整数溢出处理（使用 left + (right - left) / 2 而不是 (left + right) / 2）
 * 3. 异常输入处理（空数组）
 * 4. 可配置的比较策略
 */

#include <stdio.h>

// 基础二分查找函数
int binarySearch(int nums[], int size, int target) {
    // 异常处理
    if (nums == 0 || size <= 0) {
        return -1;
    }
    
    int left = 0;
    int right = size - 1;
    
    // 循环条件：left <= right
    while (left <= right) {
        // 防止整数溢出的中点计算方式
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            return mid;  // 找到目标值，返回索引
        } else if (nums[mid] < target) {
            left = mid + 1;  // 目标值在右半部分
        } else {
            right = mid - 1; // 目标值在左半部分
        }
    }
    
    return -1;  // 未找到目标值
}

// 查找第一个等于目标值的元素
int findFirst(int nums[], int size, int target) {
    if (nums == 0 || size <= 0) {
        return -1;
    }
    
    int left = 0;
    int right = size - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            result = mid;    // 记录找到的位置
            right = mid - 1; // 继续在左半部分查找
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

// 查找最后一个等于目标值的元素
int findLast(int nums[], int size, int target) {
    if (nums == 0 || size <= 0) {
        return -1;
    }
    
    int left = 0;
    int right = size - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            result = mid;   // 记录找到的位置
            left = mid + 1; // 继续在右半部分查找
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

// 查找第一个大于等于目标值的元素
int findFirstGreaterOrEqual(int nums[], int size, int target) {
    if (nums == 0 || size <= 0) {
        return -1;
    }
    
    int left = 0;
    int right = size - 1;
    int result = size; // 如果没找到，返回数组长度
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] >= target) {
            result = mid;   // 记录可能的位置
            right = mid - 1; // 继续在左半部分查找
        } else {
            left = mid + 1;
        }
    }
    
    return result;
}

// 查找最后一个小于等于目标值的元素
int findLastLessOrEqual(int nums[], int size, int target) {
    if (nums == 0 || size <= 0) {
        return -1;
    }
    
    int left = 0;
    int right = size - 1;
    int result = -1;
    
    while (left <= right) {
        int mid = left + (right - left) / 2;
        
        if (nums[mid] <= target) {
            result = mid;   // 记录可能的位置
            left = mid + 1; // 继续在右半部分查找
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

int main() {
    printf("测试基础二分查找...\n");
    
    // 测试基础二分查找
    int nums1[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    int size1 = sizeof(nums1) / sizeof(nums1[0]);
    printf("在数组 [1,2,3,4,5,6,7,8,9] 中查找 5: %d\n", binarySearch(nums1, size1, 5));
    printf("在数组 [1,2,3,4,5,6,7,8,9] 中查找 10: %d\n", binarySearch(nums1, size1, 10));
    
    // 测试查找第一个等于目标值的元素
    int nums2[] = {1, 2, 2, 2, 3, 4, 5};
    int size2 = sizeof(nums2) / sizeof(nums2[0]);
    printf("\n查找第一个等于目标值的元素测试：\n");
    printf("在数组 [1,2,2,2,3,4,5] 中查找第一个 2: %d\n", findFirst(nums2, size2, 2));
    
    // 测试查找最后一个等于目标值的元素
    printf("查找最后一个等于目标值的元素测试：\n");
    printf("在数组 [1,2,2,2,3,4,5] 中查找最后一个 2: %d\n", findLast(nums2, size2, 2));
    
    // 测试查找第一个大于等于目标值的元素
    printf("\n查找第一个大于等于目标值的元素测试：\n");
    printf("在数组 [1,2,3,4,5] 中查找第一个 >= 3 的元素: %d\n", findFirstGreaterOrEqual(nums1, size1, 3));
    printf("在数组 [1,2,3,4,5] 中查找第一个 >= 6 的元素: %d\n", findFirstGreaterOrEqual(nums1, size1, 6));
    
    // 测试查找最后一个小于等于目标值的元素
    printf("\n查找最后一个小于等于目标值的元素测试：\n");
    printf("在数组 [1,2,3,4,5] 中查找最后一个 <= 3 的元素: %d\n", findLastLessOrEqual(nums1, size1, 3));
    printf("在数组 [1,2,3,4,5] 中查找最后一个 <= 0 的元素: %d\n", findLastLessOrEqual(nums1, size1, 0));
    
    return 0;
}