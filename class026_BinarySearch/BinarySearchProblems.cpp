/**
 * 二分查找算法实现与相关题目解答 (C++版本)
 * 
 * 本文件包含了二分查找的各种实现和在不同场景下的应用
 * 涵盖了LeetCode、Codeforces、SPOJ等平台的经典题目
 */

#include <iostream>
#include <algorithm>
#include <climits>
#include <cstdio>
#include <vector>
#include <string>
#include <cmath>
using namespace std;

// 定义数组最大长度
const int MAXN = 100005;

/**
 * 基础二分查找
 * 在有序数组中查找目标值
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param n 数组长度
 * @param target 目标值
 * @return 目标值的索引，如果不存在返回-1
 */
int search(int nums[], int n, int target) {
    int left = 0, right = n - 1;
    while (left <= right) {
        // 使用位运算避免整数溢出
        int mid = left + ((right - left) >> 1);
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}



// 函数声明
int mySqrt(int x);
int guessNumber(int n);
int minEatingSpeed(int piles[], int n, int h);
int shipWithinDays(int weights[], int n, int days);
int minBullets(int points[][2], int n);
int guess(int num);

// 新增函数声明
int minNumberInRotateArray(int nums[], int n);
double cubeRoot(double n);
bool findClosestSum(int A[], int B[], int C[], int L, int M, int N, int X);
int monthlyExpense(int expenses[], int n, int M);
int cutTrees(int trees[], int n, long long M);
int angryCows(int haybales[], int n);
int minimizeMaximum(int nums[], int n, int K);
int jumpStones(int rocks[], int n, int M, int L);
void findWormPiles(int piles[], int n, int queries[], int m, int results[]);

// 主函数用于测试所有二分查找问题
int main() {
    // 测试LeetCode 69: Sqrt(x)
    cout << "\nLeetCode 69: Sqrt(x)测试:" << endl;
    cout << "sqrt(4) = " << mySqrt(4) << endl;  // 应该输出2
    cout << "sqrt(8) = " << mySqrt(8) << endl;  // 应该输出2
    cout << "sqrt(256) = " << mySqrt(256) << endl;  // 应该输出16
    cout << "sqrt(2147395600) = " << mySqrt(2147395600) << endl;  // 应该输出46340
    
    // 测试LeetCode 374: 猜数字大小
    cout << "\nLeetCode 374: 猜数字大小测试:" << endl;
    cout << "猜数字(10) = " << guessNumber(10) << endl;  // 应该输出6（假设pick=6）
    cout << "猜数字(1) = " << guessNumber(1) << endl;  // 应该输出1
    
    // 测试LeetCode 875: 爱吃香蕉的珂珂
    cout << "\nLeetCode 875: 爱吃香蕉的珂珂测试:" << endl;
    int piles1[] = {3, 6, 7, 11};
    int h1 = 8;
    cout << "最小吃香蕉速度(8小时): " << minEatingSpeed(piles1, 4, h1) << endl;  // 应该输出4
    
    int piles2[] = {30, 11, 23, 4, 20};
    int h2 = 5;
    cout << "最小吃香蕉速度(5小时): " << minEatingSpeed(piles2, 5, h2) << endl;  // 应该输出30
    
    // 测试LeetCode 1011: 在D天内送达包裹的能力
    cout << "\nLeetCode 1011: 在D天内送达包裹的能力测试:" << endl;
    int weights1[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int days1 = 5;
    cout << "最小运载能力(5天): " << shipWithinDays(weights1, 10, days1) << endl;  // 应该输出15
    
    int weights2[] = {3, 2, 2, 4, 1, 4};
    int days2 = 3;
    cout << "最小运载能力(3天): " << shipWithinDays(weights2, 6, days2) << endl;  // 应该输出6
    
    // 测试AtCoder ABC023D: 射撃王
    cout << "\nAtCoder ABC023D: 射撃王测试:" << endl;
    int points1[][2] = {{0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}};
    cout << "最少子弹数: " << minBullets(points1, 5) << endl;  // 应该输出1
    
    int points2[][2] = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
    cout << "最少子弹数: " << minBullets(points2, 4) << endl;  // 应该输出2
    
    // 测试新增题目
    cout << "\n=== 新增各大平台题目测试 ===" << endl;
    
    // 测试牛客网题目: 旋转数组的最小数字
    cout << "\n牛客网题目: 旋转数组的最小数字测试:" << endl;
    int rotate_nums1[] = {3, 4, 5, 1, 2};
    cout << "旋转数组最小值: " << minNumberInRotateArray(rotate_nums1, 5) << endl;  // 应该输出1
    int rotate_nums2[] = {2, 2, 2, 0, 1};
    cout << "旋转数组最小值: " << minNumberInRotateArray(rotate_nums2, 5) << endl;  // 应该输出0
    
    // 测试acwing题目: 数的三次方根
    cout << "\nacwing题目: 数的三次方根测试:" << endl;
    cout << "8的三次方根: " << cubeRoot(8.0) << endl;  // 应该输出2.0
    cout << "27的三次方根: " << cubeRoot(27.0) << endl;  // 应该输出3.0
    cout << "1000的三次方根: " << cubeRoot(1000.0) << endl;  // 应该输出10.0
    
    // 测试杭电OJ题目: 查找最接近的元素
    cout << "\n杭电OJ题目: 查找最接近的元素测试:" << endl;
    int A[] = {1, 2, 3};
    int B[] = {4, 5, 6};
    int C[] = {7, 8, 9};
    cout << "查找最接近和(15): " << (findClosestSum(A, B, C, 3, 3, 3, 15) ? "true" : "false") << endl;  // 应该输出true
    
    // 测试POJ题目: 月度开销
    cout << "\nPOJ题目: 月度开销测试:" << endl;
    int expenses[] = {100, 200, 300, 400, 500};
    cout << "月度开销(3段): " << monthlyExpense(expenses, 5, 3) << endl;  // 应该输出500
    
    // 测试洛谷题目: 砍树
    cout << "\n洛谷题目: 砍树测试:" << endl;
    int trees[] = {20, 15, 10, 17};
    cout << "砍树(7米): " << cutTrees(trees, 4, 7) << endl;  // 应该输出15
    
    // 测试USACO题目: 愤怒的奶牛
    cout << "\nUSACO题目: 愤怒的奶牛测试:" << endl;
    int haybales[] = {1, 2, 4, 8, 9};
    cout << "愤怒的奶牛: " << angryCows(haybales, 5) << endl;  // 应该输出4
    
    // 测试HackerRank题目: 最小化最大值
    cout << "\nHackerRank题目: 最小化最大值测试:" << endl;
    int nums[] = {1, 2, 3, 4, 5};
    cout << "最小化最大值(3个数): " << minimizeMaximum(nums, 5, 3) << endl;  // 应该输出2
    
    // 测试计蒜客题目: 跳石头
    cout << "\n计蒜客题目: 跳石头测试:" << endl;
    int rocks[] = {2, 11, 14, 17, 21};
    cout << "跳石头(移2块,长度25): " << jumpStones(rocks, 5, 2, 25) << endl;  // 应该输出4
    
    // 测试Codeforces题目: Worms
    cout << "\nCodeforces题目: Worms测试:" << endl;
    int piles[] = {1, 3, 2, 4};
    int queries[] = {1, 4, 7, 10};
    int results[4];
    findWormPiles(piles, 4, queries, 4, results);
    cout << "Worms查询结果: ";
    for (int i = 0; i < 4; i++) {
        cout << results[i];
        if (i < 3) cout << ", ";
    }
    cout << endl;  // 应该输出1, 2, 3, 4
    
    return 0;
}

/**
 * 查找插入位置
 * 在有序数组中查找目标值应该插入的位置
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param n 数组长度
 * @param target 目标值
 * @return 插入位置索引
 */
int searchInsert(int nums[], int n, int target) {
    int left = 0, right = n;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    return left;
}

/**
 * 辅助方法：查找>=target的最左位置
 */
int findLeft(int nums[], int n, int target) {
    int l = 0, r = n;
    while (l < r) {
        int m = l + ((r - l) >> 1);
        if (nums[m] < target) {
            l = m + 1;
        } else {
            r = m;
        }
    }
    return l;
}

/**
 * 辅助方法：查找<=target的最右位置
 */
int findRight(int nums[], int n, int target) {
    int l = 0, r = n;
    while (l < r) {
        int m = l + ((r - l) >> 1);
        if (nums[m] <= target) {
            l = m + 1;
        } else {
            r = m;
        }
    }
    return l - 1;
}

/**
 * 查找元素的第一个和最后一个位置
 * 在有序数组中查找目标值的起始和结束位置
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 有序数组
 * @param n 数组长度
 * @param target 目标值
 * @param result 返回结果数组，长度为2
 */
void searchRange(int nums[], int n, int target, int result[]) {
    // 边界条件检查
    if (n == 0) {
        result[0] = -1;
        result[1] = -1;
        return;
    }
    
    int first = findLeft(nums, n, target);
    // 如果找不到>=target的元素，或者该元素不等于target，则说明target不存在
    if (first == n || nums[first] != target) {
        result[0] = -1;
        result[1] = -1;
        return;
    }
    
    int last = findRight(nums, n, target);
    result[0] = first;
    result[1] = last;
}

/**
 * 有效的完全平方数
 * 判断一个数是否是完全平方数
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param num 正整数
 * @return 如果是完全平方数返回true，否则返回false
 */
bool isPerfectSquare(int num) {
    // 边界条件检查
    if (num < 1) {
        return false;
    }
    if (num == 1) {
        return true;
    }
    
    long long l = 1, r = num / 2; // 一个数的平方根不会超过它的一半(除了1)
    while (l <= r) {
        long long m = l + ((r - l) >> 1);
        long long square = m * m;
        if (square == num) {
            return true;
        } else if (square > num) {
            r = m - 1;
        } else {
            l = m + 1;
        }
    }
    return false;
}

/**
 * 模拟API：判断版本是否错误
 */
bool isBadVersion(int version) {
    // 这里只是一个示例实现
    // 实际应用中会根据具体需求实现
    return version >= 4; // 假设第4个版本开始是错误的
}

/**
 * 第一个错误的版本
 * 查找第一个错误的版本
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param n 版本总数
 * @return 第一个错误的版本号
 */
int firstBadVersion(int n) {
    int left = 1, right = n;
    while (left < right) {
        // 假设存在 isBadVersion API
        int mid = left + ((right - left) >> 1);
        if (isBadVersion(mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 寻找峰值元素
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 数组
 * @param n 数组长度
 * @return 峰值元素的索引
 */
int findPeakElement(int nums[], int n) {
    int left = 0, right = n - 1;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (nums[mid] > nums[mid + 1]) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 寻找旋转排序数组中的最小值
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 旋转排序数组
 * @param n 数组长度
 * @return 数组中的最小值
 */
int findMin(int nums[], int n) {
    int left = 0, right = n - 1;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (nums[mid] < nums[right]) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return nums[left];
}

/**
 * 搜索二维矩阵
 * 
 * 时间复杂度: O(log(m*n))
 * 空间复杂度: O(1)
 * 
 * @param matrix 二维矩阵
 * @param m 矩阵行数
 * @param n 矩阵列数
 * @param target 目标值
 * @return 如果找到目标值返回true，否则返回false
 */
bool searchMatrix(int matrix[][MAXN], int m, int n, int target) {
    int left = 0, right = m * n - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        int midValue = matrix[mid / n][mid % n];
        
        if (midValue == target) {
            return true;
        } else if (midValue < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return false;
}

/**
 * 搜索旋转排序数组
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 旋转排序数组
 * @param n 数组长度
 * @param target 目标值
 * @return 目标值的索引，如果不存在返回-1
 */
int searchInRotatedSortedArray(int nums[], int n, int target) {
    int left = 0, right = n - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == target) {
            return mid;
        }
        
        // 左半部分有序
        if (nums[left] <= nums[mid]) {
            if (nums[left] <= target && target < nums[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        } 
        // 右半部分有序
        else {
            if (nums[mid] < target && target <= nums[right]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return -1;
}

/**
 * 寻找重复数
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(1)
 * 
 * @param nums 包含重复数字的数组
 * @param n 数组长度
 * @return 重复的数字
 */
int findDuplicate(int nums[], int n) {
    int left = 1, right = n - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        int count = 0;
        
        // 计算小于等于mid的数字个数
        for (int i = 0; i < n; i++) {
            if (nums[i] <= mid) {
                count++;
            }
        }
        
        // 根据抽屉原理判断重复数字在哪一侧
        if (count > mid) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}

/**
 * 辅助方法：检查是否能以给定最小距离放置奶牛
 */
bool canPlaceCows(int positions[], int n, int cows, int minDist) {
    int count = 1;
    int lastPosition = positions[0];
    
    for (int i = 1; i < n; i++) {
        if (positions[i] - lastPosition >= minDist) {
            count++;
            lastPosition = positions[i];
            if (count == cows) {
                return true;
            }
        }
    }
    
    return false;
}

/**
 * SPOJ - AGGRCOW (Aggressive cows)
 * 
 * 时间复杂度: O(n log(max-min) * n)
 * 空间复杂度: O(1)
 * 
 * @param positions 摊位位置数组
 * @param n 位置数组长度
 * @param cows 奶牛数量
 * @return 最大化最小距离
 */
int aggressiveCows(int positions[], int n, int cows) {
    // 假设数组已经排序
    int left = 0, right = positions[n-1] - positions[0];
    int result = 0;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        if (canPlaceCows(positions, n, cows, mid)) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return result;
}

/**
 * 带异常处理的安全二分查找
 * 
 * @param nums 有序数组
 * @param n 数组长度
 * @param target 目标值
 * @return 目标值的索引，如果不存在返回-1
 */
int safeBinarySearch(int nums[], int n, int target) {
    // 检查数组是否已排序
    for (int i = 1; i < n; i++) {
        if (nums[i] < nums[i-1]) {
            // 在实际应用中可以抛出异常
            // 这里简单返回-1表示错误
            return -1;
        }
    }
    
    int left = 0, right = n - 1;
    
    while (left <= right) {
        // 防止整数溢出的中点计算
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}

/**
 * LeetCode 69. Sqrt(x)
 * 
 * 时间复杂度: O(log x)
 * 空间复杂度: O(1)
 * 
 * @param x 输入的非负整数
 * @return x 的平方根的整数部分
 */
int mySqrt(int x) {
    if (x == 0 || x == 1) {
        return x;
    }
    
    long long left = 1, right = x;
    while (left <= right) {
        long long mid = left + ((right - left) >> 1);
        
        // 防止整数溢出，使用long long类型
        if (mid * mid > x) {
            right = mid - 1;
        } else {
            // 如果下一个值会溢出或者大于x，则当前mid是最大的有效平方根
            if ((mid + 1) * (mid + 1) > x) {
                return mid;
            }
            left = mid + 1;
        }
    }
    
    return right;
}

/**
 * LeetCode 374. 猜数字大小
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 
 * @param n 数字范围上限
 * @return 猜中的数字
 */
int guessNumber(int n) {
    long long left = 1, right = n;
    
    while (left <= right) {
        // 防止整数溢出
        long long mid = left + ((right - left) >> 1);
        int result = guess(mid);
        
        if (result == 0) {
            return mid;  // 猜对了
        } else if (result == 1) {
            left = mid + 1;  // 猜小了，往右边找
        } else {
            right = mid - 1;  // 猜大了，往左边找
        }
    }
    
    return -1;  // 理论上不会执行到这里
}

// 示例的guess函数，实际会由系统提供
int guess(int num) {
    int pick = 6;  // 假设选中的数字是6
    if (num < pick) return 1;
    else if (num > pick) return -1;
    else return 0;
}

/**
 * 判断以速度speed是否能在h小时内吃完所有香蕉
 */
bool canFinish(int piles[], int n, int h, int speed) {
    long long time = 0;
    
    for (int i = 0; i < n; i++) {
        // 计算吃完当前堆需要的时间
        time += (piles[i] + speed - 1) / speed;  // 等价于 ceil(piles[i] / speed)
        
        // 如果时间已经超过h，可以提前返回false
        if (time > h) {
            return false;
        }
    }
    
    return time <= h;
}

/**
 * LeetCode 875. 爱吃香蕉的珂珂
 * 
 * 时间复杂度: O(n log maxPile)
 * 空间复杂度: O(1)
 * 
 * @param piles 香蕉堆数组
 * @param n 数组长度
 * @param h 警卫离开的时间（小时）
 * @return 最小的吃香蕉速度
 */
int minEatingSpeed(int piles[], int n, int h) {
    int left = 1;
    int right = 0;
    
    // 找到最大的香蕉堆，作为右边界
    for (int i = 0; i < n; i++) {
        if (piles[i] > right) {
            right = piles[i];
        }
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canFinish(piles, n, h, mid)) {
            right = mid;  // 可以吃完，尝试更小的速度
        } else {
            left = mid + 1;  // 不能吃完，需要更大的速度
        }
    }
    
    return left;
}

/**
 * 判断以capacity的运载能力是否能在days天内运完所有包裹
 */
bool canShipInDays(int weights[], int n, int days, int capacity) {
    int currentWeight = 0;
    int dayCount = 1;  // 至少需要1天
    
    for (int i = 0; i < n; i++) {
        // 如果当前包裹的重量已经超过了运载能力，不可能运完
        if (weights[i] > capacity) {
            return false;
        }
        
        // 如果当前累计重量加上当前包裹的重量超过了运载能力，需要新的一天
        if (currentWeight + weights[i] > capacity) {
            dayCount++;
            currentWeight = weights[i];  // 新的一天从当前包裹开始
            
            // 如果天数已经超过了限制，可以提前返回false
            if (dayCount > days) {
                return false;
            }
        } else {
            currentWeight += weights[i];  // 继续往当前天添加包裹
        }
    }
    
    return dayCount <= days;
}

/**
 * LeetCode 1011. 在D天内送达包裹的能力
 * 
 * 时间复杂度: O(n log totalWeight)
 * 空间复杂度: O(1)
 * 
 * @param weights 包裹重量数组
 * @param n 数组长度
 * @param days 天数限制
 * @return 船的最小运载能力
 */
int shipWithinDays(int weights[], int n, int days) {
    int left = 0;  // 最小运载能力：最大的单个包裹重量
    int right = 0; // 最大运载能力：所有包裹的总重量
    
    for (int i = 0; i < n; i++) {
        if (weights[i] > left) {
            left = weights[i];
        }
        right += weights[i];
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canShipInDays(weights, n, days, mid)) {
            right = mid;  // 可以在days天内运完，尝试更小的运载能力
        } else {
            left = mid + 1;  // 不能在days天内运完，需要更大的运载能力
        }
    }
    
    return left;
}

/**
 * 判断三个点是否共线
 */
bool isCollinear(int p1[2], int p2[2], int p3[2]) {
    // 使用叉积判断三点共线
    // (y2 - y1) * (x3 - x1) == (y3 - y1) * (x2 - x1)
    return (p2[1] - p1[1]) * (p3[0] - p1[0]) == (p3[1] - p1[1]) * (p2[0] - p1[0]);
}

/**
 * 递归辅助函数：尝试覆盖剩余的点
 */
bool coverRecursive(int points[][2], bool covered[], int n, int coveredCount, int remainingLines) {
    if (coveredCount == n) return true;
    if (remainingLines == 0) return false;
    
    // 找到第一个未覆盖的点
    int first = -1;
    for (int i = 0; i < n; i++) {
        if (!covered[i]) {
            first = i;
            break;
        }
    }
    
    // 尝试通过第一个未覆盖的点画一条直线
    for (int i = 0; i < n; i++) {
        if (i == first || covered[i]) continue;
        
        // 标记所有在这条直线上的点
        bool* newCovered = new bool[n];
        for (int j = 0; j < n; j++) {
            newCovered[j] = covered[j];
        }
        int newCount = coveredCount;
        
        for (int j = 0; j < n; j++) {
            if (!newCovered[j] && isCollinear(points[first], points[i], points[j])) {
                newCovered[j] = true;
                newCount++;
            }
        }
        
        bool result = coverRecursive(points, newCovered, n, newCount, remainingLines - 1);
        delete[] newCovered;
        
        if (result) {
            return true;
        }
    }
    
    // 如果没有其他点，可以单独用一条直线覆盖第一个未覆盖的点
    return coverRecursive(points, covered, n, coveredCount + 1, remainingLines - 1);
}

/**
 * AtCoder ABC023D - 射撃王 (Shooting King)
 * 
 * 时间复杂度: O(N^3 log N)
 * 空间复杂度: O(N)
 * 
 * @param points 目标点数组，每个点是[x, y]
 * @param n 点的数量
 * @return 最少需要的子弹数
 */
int minBullets(int points[][2], int n) {
    if (n == 0) return 0;
    if (n == 1) return 1;
    
    int left = 1, right = n;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        bool* covered = new bool[n]();
        bool canCoverAll = coverRecursive(points, covered, n, 0, mid);
        delete[] covered;
        
        if (canCoverAll) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

/**
 * 性能优化版本的二分查找
 * 
 * @param nums 有序数组
 * @param n 数组长度
 * @param target 目标值
 * @return 目标值的索引，如果不存在返回-1
 */
int optimizedBinarySearch(int nums[], int n, int target) {
    int left = 0, right = n - 1;
    
    while (left <= right) {
        // 使用无符号右移避免溢出
        int mid = (left + right) >> 1;
        
        if (nums[mid] == target) {
            return mid;
        }
        
        // 使用位运算优化比较
        if (nums[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}

/**
 * 牛客网: 旋转数组的最小数字
 * 题目来源: https://www.nowcoder.com/practice/9f3231a991af4f55b95579b44b7a01ba
 * 
 * 题目描述:
 * 把一个数组最开始的若干个元素搬到数组的末尾,我们称之为数组的旋转。
 * 输入一个非递减排序的数组的一个旋转,输出旋转数组的最小元素。
 * 
 * 思路分析:
 * 1. 二分查找法,比较中间元素与右边界元素
 * 2. 如果中间元素小于右边界,最小值在左侧
 * 3. 如果中间元素大于右边界,最小值在右侧
 * 4. 如果相等,右边界左移一位
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 旋转数组
 * @param n 数组长度
 * @return 最小元素
 */
int minNumberInRotateArray(int nums[], int n) {
    if (n == 0) return -1;
    
    int left = 0, right = n - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] < nums[right]) {
            right = mid;
        } else if (nums[mid] > nums[right]) {
            left = mid + 1;
        } else {
            right--;
        }
    }
    
    return nums[left];
}

/**
 * acwing: 数的三次方根
 * 题目来源: https://www.acwing.com/problem/content/792/
 * 
 * 题目描述:
 * 给定一个浮点数n,求它的三次方根,结果保留6位小数。
 * 
 * 思路分析:
 * 1. 二分查找法,在[-10000, 10000]范围内搜索
 * 2. 精度控制到1e-8
 * 3. 根据mid^3与n的大小关系调整搜索区间
 * 
 * 时间复杂度: O(log(范围/精度))
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param n 输入浮点数
 * @return 三次方根
 */
double cubeRoot(double n) {
    double left = -10000, right = 10000;
    
    while (right - left > 1e-8) {
        double mid = (left + right) / 2;
        
        if (mid * mid * mid >= n) {
            right = mid;
        } else {
            left = mid;
        }
    }
    
    return left;
}

/**
 * 牛客网: 二维数组中的查找
 * 题目来源: https://www.nowcoder.com/practice/abc3fe2ce8e146608e868a70efebf62e
 * 
 * 题目描述:
 * 在一个二维数组中,每一行都按照从左到右递增的顺序排序,每一列都按照从上到下递增的顺序排序。
 * 请完成一个函数,输入这样的一个二维数组和一个整数,判断数组中是否含有该整数。
 * 
 * 思路分析:
 * 1. 从右上角开始搜索
 * 2. 如果当前元素等于目标值,返回true
 * 3. 如果当前元素大于目标值,向左移动
 * 4. 如果当前元素小于目标值,向下移动
 * 
 * 时间复杂度: O(m+n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param matrix 二维数组
 * @param rows 行数
 * @param cols 列数
 * @param target 目标值
 * @return 是否找到目标值
 */
bool findIn2DArray(int matrix[][MAXN], int rows, int cols, int target) {
    if (rows == 0 || cols == 0) return false;
    
    int row = 0, col = cols - 1;
    
    while (row < rows && col >= 0) {
        if (matrix[row][col] == target) {
            return true;
        } else if (matrix[row][col] > target) {
            col--;
        } else {
            row++;
        }
    }
    
    return false;
}

/**
 * 牛客网: 数字序列中某一位的数字
 * 题目来源: https://www.nowcoder.com/practice/29311ff7404d44e0b07077f4201418f5
 * 
 * 题目描述:
 * 数字以0123456789101112131415...的格式序列化到一个字符序列中。
 * 在这个序列中,第5位(从0开始计数)是5,第13位是1,第19位是4,等等。
 * 请写一个函数,求任意第n位对应的数字。
 * 
 * 思路分析:
 * 1. 确定n所在的数字位数范围
 * 2. 确定具体的数字
 * 3. 确定数字中的具体位
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param n 位置索引
 * @return 对应的数字
 */
int digitAtIndex(int n) {
    if (n < 0) return -1;
    if (n < 10) return n;
    
    long long digits = 1;  // 当前位数
    long long count = 9;   // 当前位数下的数字个数
    long long start = 1;   // 当前位数下的起始数字
    
    // 确定n所在的数字位数
    while (n > digits * count) {
        n -= digits * count;
        digits++;
        count *= 10;
        start *= 10;
    }
    
    // 确定具体的数字
    long long num = start + (n - 1) / digits;
    
    // 确定数字中的具体位
    string numStr = to_string(num);
    return numStr[(n - 1) % digits] - '0';
}

/**
 * 牛客网: 0到n-1中缺失的数字
 * 题目来源: https://www.nowcoder.com/practice/9ce534c8132b4e189fd3130519420cde
 * 
 * 题目描述:
 * 一个长度为n-1的递增排序数组中的所有数字都是唯一的,并且每个数字都在范围0到n-1之内。
 * 在范围0到n-1内的n个数字中有且只有一个数字不在该数组中,请找出这个数字。
 * 
 * 思路分析:
 * 1. 二分查找法,比较中间元素的值与索引
 * 2. 如果nums[mid] == mid,缺失数字在右侧
 * 3. 如果nums[mid] != mid,缺失数字在左侧
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 递增排序数组
 * @param n 数组长度(实际为n-1)
 * @return 缺失的数字
 */
int missingNumber(int nums[], int n) {
    int left = 0, right = n - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == mid) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return left;
}

/**
 * 牛客网: 数组中数值和下标相等的元素
 * 题目来源: https://www.nowcoder.com/practice/7bc4a1c7c371425d9faa9d1b511fe193
 * 
 * 题目描述:
 * 假设一个单调递增的数组里的每个元素都是整数并且是唯一的。
 * 请编程实现一个函数找出数组中任意一个数值等于其下标的元素。
 * 
 * 思路分析:
 * 1. 二分查找法,比较元素值与索引
 * 2. 如果nums[mid] == mid,找到目标
 * 3. 如果nums[mid] < mid,目标在右侧
 * 4. 如果nums[mid] > mid,目标在左侧
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 单调递增数组
 * @param n 数组长度
 * @return 数值等于下标的元素,不存在返回-1
 */
int numberEqualIndex(int nums[], int n) {
    int left = 0, right = n - 1;
    
    while (left <= right) {
        int mid = left + ((right - left) >> 1);
        
        if (nums[mid] == mid) {
            return mid;
        } else if (nums[mid] < mid) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    
    return -1;
}

/**
 * 牛客网: 和为S的两个数字
 * 题目来源: https://www.nowcoder.com/practice/390da4f7a00f44bea7c2f3d19491311b
 * 
 * 题目描述:
 * 输入一个递增排序的数组和一个数字S,在数组中查找两个数,使得它们的和正好是S。
 * 如果有多对数字的和等于S,输出两个数的乘积最小的。
 * 
 * 思路分析:
 * 1. 双指针法,一个指向开头,一个指向结尾
 * 2. 计算两数之和,与S比较
 * 3. 如果和等于S,记录乘积最小的组合
 * 4. 如果和小于S,左指针右移
 * 5. 如果和大于S,右指针左移
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 递增排序数组
 * @param n 数组长度
 * @param S 目标和
 * @param result 结果数组
 */
void findNumbersWithSum(int nums[], int n, int S, int result[]) {
    if (n < 2) {
        result[0] = result[1] = -1;
        return;
    }
    
    int left = 0, right = n - 1;
    int minProduct = INT_MAX;
    int num1 = -1, num2 = -1;
    
    while (left < right) {
        int sum = nums[left] + nums[right];
        
        if (sum == S) {
            int product = nums[left] * nums[right];
            if (product < minProduct) {
                minProduct = product;
                num1 = nums[left];
                num2 = nums[right];
            }
            left++;
            right--;
        } else if (sum < S) {
            left++;
        } else {
            right--;
        }
    }
    
    result[0] = num1;
    result[1] = num2;
}

/**
 * 牛客网: 和为S的连续正数序列
 * 题目来源: https://www.nowcoder.com/practice/c451a3fd84b64cb19485dad758a55ebe
 * 
 * 题目描述:
 * 输入一个正数S,打印出所有和为S的连续正数序列(至少含有两个数)。
 * 
 * 思路分析:
 * 1. 滑动窗口法,维护一个连续序列的窗口
 * 2. 窗口和小于S时,右边界右移
 * 3. 窗口和大于S时,左边界右移
 * 4. 窗口和等于S时,记录序列
 * 
 * 时间复杂度: O(S)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param S 目标和
 * @param sequences 存储结果的二维数组
 * @param count 序列数量
 */
void findContinuousSequence(int S, int sequences[][MAXN], int* count) {
    *count = 0;
    if (S < 3) return;
    
    int left = 1, right = 2;
    int sum = 3;
    
    while (left < right && right <= (S + 1) / 2) {
        if (sum == S) {
            // 记录当前序列
            int len = right - left + 1;
            for (int i = 0; i < len; i++) {
                sequences[*count][i] = left + i;
            }
            (*count)++;
            sum -= left;
            left++;
        } else if (sum < S) {
            right++;
            sum += right;
        } else {
            sum -= left;
            left++;
        }
    }
}

/**
 * acwing: 数的范围
 * 题目来源: https://www.acwing.com/problem/content/791/
 * 
 * 题目描述:
 * 给定一个按照升序排列的长度为n的整数数组,以及q个查询。
 * 对于每个查询,返回一个元素k的起始位置和终止位置(位置从0开始计数)。
 * 如果数组中不存在该元素,则返回"-1 -1"。
 * 
 * 思路分析:
 * 1. 使用两次二分查找
 * 2. 第一次查找左边界:找到第一个>=k的位置
 * 3. 第二次查找右边界:找到最后一个<=k的位置
 * 4. 检查边界是否有效
 * 
 * 时间复杂度: O(q * log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 升序数组
 * @param n 数组长度
 * @param k 查询元素
 * @param result 结果数组[左边界, 右边界]
 */
void findRange(int nums[], int n, int k, int result[]) {
    // 查找左边界
    int left = 0, right = n - 1;
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        if (nums[mid] >= k) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    if (nums[left] != k) {
        result[0] = result[1] = -1;
        return;
    }
    
    result[0] = left;
    
    // 查找右边界
    right = n - 1;
    while (left < right) {
        int mid = left + ((right - left) >> 1) + 1;  // 向上取整
        if (nums[mid] <= k) {
            left = mid;
        } else {
            right = mid - 1;
        }
    }
    
    result[1] = left;
}

/**
 * acwing: 四平方和
 * 题目来源: https://www.acwing.com/problem/content/1223/
 * 
 * 题目描述:
 * 四平方和定理,又称为拉格朗日定理:每个正整数都可以表示为至多4个正整数的平方和。
 * 如果把0包括进去,就正好可以表示为4个数的平方和。
 * 
 * 思路分析:
 * 1. 预处理所有a²+b²的组合
 * 2. 对每个组合进行排序
 * 3. 二分查找是否存在c²+d² = n - (a²+b²)
 * 
 * 时间复杂度: O(n² log n)
 * 空间复杂度: O(n²)
 * 是否最优解: 是
 * 
 * @param n 目标正整数
 * @param result 结果数组[a, b, c, d]
 */
void fourSquareSum(int n, int result[]) {
    // 预处理所有a²+b²的组合
    struct Pair {
        int sum;
        int a;
        int b;
    };
    
    vector<Pair> pairs;
    for (int a = 0; a * a <= n; a++) {
        for (int b = a; a * a + b * b <= n; b++) {
            pairs.push_back({a * a + b * b, a, b});
        }
    }
    
    // 按和排序
    sort(pairs.begin(), pairs.end(), [](const Pair& p1, const Pair& p2) {
        if (p1.sum != p2.sum) return p1.sum < p2.sum;
        if (p1.a != p2.a) return p1.a < p2.a;
        return p1.b < p2.b;
    });
    
    // 二分查找
    for (int a = 0; a * a <= n; a++) {
        for (int b = a; a * a + b * b <= n; b++) {
            int target = n - a * a - b * b;
            
            int left = 0, right = pairs.size() - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                
                if (pairs[mid].sum == target) {
                    result[0] = a;
                    result[1] = b;
                    result[2] = pairs[mid].a;
                    result[3] = pairs[mid].b;
                    return;
                } else if (pairs[mid].sum < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
    }
}

/**
 * 各大算法平台二分查找题目总结
 * 
 * 工程化考量:
 * 1. 异常处理:空数组、非法输入、边界条件
 * 2. 性能优化:避免整数溢出、位运算优化
 * 3. 可读性:清晰的变量命名、详细的注释
 * 4. 测试覆盖:各种边界情况的测试用例
 * 
 * 语言特性差异:
 * - C++:使用vector、algorithm库、位运算
 * - Java:使用ArrayList、Arrays类
 * - Python:使用列表、内置函数
 * 
 * 调试技巧:
 * 1. 打印中间变量值
 * 2. 使用断言验证中间结果
 * 3. 性能分析工具定位瓶颈
 */

/**
 * LeetCode 410. 分割数组的最大值
 * 题目来源: https://leetcode.com/problems/split-array-largest-sum/
 * 
 * 题目描述:
 * 给定一个非负整数数组 nums 和一个整数 m,需要将这个数组分成 m 个非空的连续子数组。
 * 设计一个算法使得这 m 个子数组各自和的最大值最小。
 * 
 * 思路分析:
 * 1. 二分答案法:答案范围在[max(nums), sum(nums)]之间
 * 2. 对于每个可能的答案mid,检查能否将数组分成<=m个子数组且每个子数组和<=mid
 * 3. 如果可以,说明答案可能更小,向左搜索;否则向右搜索
 * 
 * 时间复杂度: O(n * log(sum - max))
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 非负整数数组
 * @param n 数组长度
 * @param m 分割的子数组数量
 * @return 最小的最大子数组和
 */
bool canSplitArray(int nums[], int n, int count, long long maxSum) {
    int splits = 1;
    long long currentSum = 0;
    
    for (int i = 0; i < n; i++) {
        if (currentSum + nums[i] > maxSum) {
            splits++;
            currentSum = nums[i];
            
            if (splits > count) {
                return false;
            }
        } else {
            currentSum += nums[i];
        }
    }
    
    return true;
}

int splitArray(int nums[], int n, int m) {
    if (n == 0 || m <= 0) {
        return 0;
    }
    
    long long left = 0;
    long long right = 0;
    
    for (int i = 0; i < n; i++) {
        if (nums[i] > left) {
            left = nums[i];
        }
        right += nums[i];
    }
    
    while (left < right) {
        long long mid = left + ((right - left) >> 1);
        
        if (canSplitArray(nums, n, m, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return (int) left;
}

/**
 * LeetCode 4. 寻找两个正序数组的中位数
 * 题目来源: https://leetcode.com/problems/median-of-two-sorted-arrays/
 * 
 * 题目描述:
 * 给定两个大小分别为 m 和 n 的正序(从小到大)数组 nums1 和 nums2。
 * 请你找出并返回这两个正序数组的中位数。
 * 
 * 思路分析:
 * 1. 使用二分查找在较短的数组上进行分割
 * 2. 确保左半部分的最大值 <= 右半部分的最小值
 * 3. 中位数由左半部分最大值和右半部分最小值决定
 * 
 * 时间复杂度: O(log(min(m,n)))
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums1 第一个正序数组
 * @param m nums1的长度
 * @param nums2 第二个正序数组
 * @param n nums2的长度
 * @return 两个数组的中位数
 */
double findMedianSortedArrays(int nums1[], int m, int nums2[], int n) {
    // 确保nums1是较短的数组
    if (m > n) {
        return findMedianSortedArrays(nums2, n, nums1, m);
    }
    
    int left = 0, right = m;
    
    while (left <= right) {
        int partition1 = left + ((right - left) >> 1);
        int partition2 = ((m + n + 1) >> 1) - partition1;
        
        int maxLeft1 = (partition1 == 0) ? INT_MIN : nums1[partition1 - 1];
        int minRight1 = (partition1 == m) ? INT_MAX : nums1[partition1];
        
        int maxLeft2 = (partition2 == 0) ? INT_MIN : nums2[partition2 - 1];
        int minRight2 = (partition2 == n) ? INT_MAX : nums2[partition2];
        
        if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
            if ((m + n) % 2 == 0) {
                return (max(maxLeft1, maxLeft2) + min(minRight1, minRight2)) / 2.0;
            } else {
                return max(maxLeft1, maxLeft2);
            }
        } else if (maxLeft1 > minRight2) {
            right = partition1 - 1;
        } else {
            left = partition1 + 1;
        }
    }
    
    return 0.0;
}

/**
 * 牛客/剑指Offer: 数字在排序数组中出现的次数
 * 题目来源: https://www.nowcoder.com/practice/70610bf967994b22bb1c26f9ae901fa2
 * 
 * 题目描述:
 * 统计一个数字在排序数组中出现的次数。
 * 
 * 思路分析:
 * 使用二分查找找到目标值的左右边界,次数 = 右边界 - 左边界 + 1
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 排序数组
 * @param n 数组长度
 * @param target 目标值
 * @return 出现次数
 */
int getNumberOfK(int nums[], int n, int target) {
    if (n == 0) {
        return 0;
    }
    
    int first = findLeft(nums, n, target);
    if (first == n || nums[first] != target) {
        return 0;
    }
    
    int last = findRight(nums, n, target);
    return last - first + 1;
}

/**
 * LeetCode 540. 有序数组中的单一元素
 * 题目来源: https://leetcode.com/problems/single-element-in-a-sorted-array/
 * 
 * 题目描述:
 * 给定一个只包含整数的有序数组,每个元素都会出现两次,唯有一个数只会出现一次,找出这个数。
 * 
 * 思路分析:
 * 1. 单一元素之前,成对元素的第一个位置是偶数索引
 * 2. 单一元素之后,成对元素的第一个位置是奇数索引
 * 3. 使用二分查找定位单一元素
 * 
 * 时间复杂度: O(log n)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 有序数组
 * @param n 数组长度
 * @return 单一元素
 */
int singleNonDuplicate(int nums[], int n) {
    int left = 0, right = n - 1;
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        // 确保mid是偶数索引
        if (mid % 2 == 1) {
            mid--;
        }
        
        // 检查成对关系
        if (nums[mid] == nums[mid + 1]) {
            // 单一元素在右侧
            left = mid + 2;
        } else {
            // 单一元素在左侧或就是mid
            right = mid;
        }
    }
    
    return nums[left];
}

/**
 * LeetCode 1482. 制作 m 束花所需的最少天数
 * 题目来源: https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/
 * 
 * 题目描述:
 * 给你一个整数数组 bloomDay,以及两个整数 m 和 k。
 * 现需要制作 m 束花。制作花束时,需要使用花园中相邻的 k 朵花。
 * 花园中有 n 朵花,第 i 朵花会在 bloomDay[i] 时盛开。
 * 请你返回从花园中摘 m 束花需要等待的最少的天数。如果不能摘到 m 束花则返回 -1。
 * 
 * 思路分析:
 * 1. 二分答案:天数范围在[min(bloomDay), max(bloomDay)]之间
 * 2. 对于每个天数,检查能否制作m束花
 * 3. 贪心验证:从左到右扫描,统计连续k朵已开花的数量
 * 
 * 时间复杂度: O(n * log(max - min))
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param bloomDay 每朵花开放的天数
 * @param n 数组长度
 * @param m 需要的花束数量
 * @param k 每束花包含的花朵数量
 * @return 最少等待天数
 */
bool canMakeBouquets(int bloomDay[], int n, int m, int k, int day) {
    int bouquets = 0;
    int flowers = 0;
    
    for (int i = 0; i < n; i++) {
        if (bloomDay[i] <= day) {
            flowers++;
            if (flowers == k) {
                bouquets++;
                flowers = 0;
                if (bouquets == m) {
                    return true;
                }
            }
        } else {
            flowers = 0;
        }
    }
    
    return bouquets >= m;
}

int minDays(int bloomDay[], int n, int m, int k) {
    if ((long long) m * k > n) {
        return -1;
    }
    
    int left = INT_MAX;
    int right = INT_MIN;
    
    for (int i = 0; i < n; i++) {
        if (bloomDay[i] < left) {
            left = bloomDay[i];
        }
        if (bloomDay[i] > right) {
            right = bloomDay[i];
        }
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canMakeBouquets(bloomDay, n, m, k, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}

/**
 * 杭电OJ: 查找最接近的元素
 * 题目来源: http://acm.hdu.edu.cn/showproblem.php?pid=2141
 * 
 * 题目描述:
 * 给定三个数组A、B、C和一个目标值X，判断是否存在三个数a∈A, b∈B, c∈C使得a+b+c=X。
 * 
 * 思路分析:
 * 1. 先计算A+B的所有可能和，存储在数组中并排序
 * 2. 对于每个c∈C，在A+B的和数组中查找X-c
 * 3. 使用二分查找提高查找效率
 * 
 * 时间复杂度: O(L*M + N*log(L*M))
 * 空间复杂度: O(L*M)
 * 是否最优解: 是
 * 
 * @param A 数组A
 * @param B 数组B
 * @param C 数组C
 * @param L 数组A长度
 * @param M 数组B长度
 * @param N 数组C长度
 * @param X 目标值
 * @return 是否存在满足条件的三元组
 */
bool findClosestSum(int A[], int B[], int C[], int L, int M, int N, int X) {
    // 计算A+B的所有可能和
    int* sumAB = new int[L * M];
    int index = 0;
    
    for (int i = 0; i < L; i++) {
        for (int j = 0; j < M; j++) {
            sumAB[index++] = A[i] + B[j];
        }
    }
    
    // 排序A+B的和数组
    sort(sumAB, sumAB + index);
    
    // 对于每个c∈C，在A+B的和数组中查找X-c
    for (int i = 0; i < N; i++) {
        int target = X - C[i];
        
        // 使用二分查找
        int left = 0, right = index - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            
            if (sumAB[mid] == target) {
                delete[] sumAB;
                return true;
            } else if (sumAB[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    delete[] sumAB;
    return false;
}

/**
 * POJ: 月度开销
 * 题目来源: http://poj.org/problem?id=3273
 * 
 * 题目描述:
 * FJ有N天的账单，他想把这N天分成M个月，使得每个月的开销之和的最大值最小。
 * 
 * 思路分析:
 * 1. 二分答案法，答案范围在[max(expenses), sum(expenses)]之间
 * 2. 对于每个可能的答案mid，检查能否将账单分成<=M个月且每个月开销<=mid
 * 3. 如果可以，说明答案可能更小，向左搜索；否则向右搜索
 * 
 * 时间复杂度: O(N * log(sum - max))
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param expenses 每天的开销数组
 * @param n 数组长度
 * @param M 月份数量
 * @return 最小的最大月开销
 */
bool canDivideExpenses(int expenses[], int n, int M, int maxExpense) {
    int months = 1;
    int currentExpense = 0;
    
    for (int i = 0; i < n; i++) {
        if (currentExpense + expenses[i] > maxExpense) {
            months++;
            currentExpense = expenses[i];
            
            if (months > M) {
                return false;
            }
        } else {
            currentExpense += expenses[i];
        }
    }
    
    return true;
}

int monthlyExpense(int expenses[], int n, int M) {
    int left = 0;
    int right = 0;
    
    for (int i = 0; i < n; i++) {
        if (expenses[i] > left) {
            left = expenses[i];
        }
        right += expenses[i];
    }
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canDivideExpenses(expenses, n, M, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}

/**
 * 洛谷: 砍树
 * 题目来源: https://www.luogu.com.cn/problem/P1873
 * 
 * 题目描述:
 * 有N棵树，第i棵树的高度是Hi。伐木工人需要获得至少M米的木材。
 * 伐木工人会选择一个高度H，将所有高度大于H的树砍掉高于H的部分。
 * 求伐木工人能选择的最大H值。
 * 
 * 思路分析:
 * 1. 二分答案法，答案范围在[0, max(trees)]之间
 * 2. 对于每个可能的高度H，计算能获得的木材总量
 * 3. 如果总量>=M，说明H可以更大，向右搜索；否则向左搜索
 * 
 * 时间复杂度: O(N * log(maxHeight))
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param trees 树的高度数组
 * @param n 数组长度
 * @param M 需要的木材量
 * @return 最大的切割高度H
 */
long long getWood(int trees[], int n, int H) {
    long long total = 0;
    
    for (int i = 0; i < n; i++) {
        if (trees[i] > H) {
            total += trees[i] - H;
        }
    }
    
    return total;
}

int cutTrees(int trees[], int n, long long M) {
    int left = 0;
    int right = 0;
    
    for (int i = 0; i < n; i++) {
        if (trees[i] > right) {
            right = trees[i];
        }
    }
    
    while (left < right) {
        int mid = left + ((right - left + 1) >> 1);
        
        if (getWood(trees, n, mid) >= M) {
            left = mid;
        } else {
            right = mid - 1;
        }
    }
    
    return left;
}

/**
 * USACO: 愤怒的奶牛
 * 题目来源: http://www.usaco.org/index.php?page=viewproblem2&cpid=764
 * 
 * 题目描述:
 * 在一条线上有N个草包，第i个草包在位置xi。愤怒的奶牛可以选择引爆一个草包，
 * 爆炸半径R会引爆所有距离<=R的草包。求引爆所有草包所需的最小爆炸半径。
 * 
 * 思路分析:
 * 1. 二分答案法，答案范围在[0, max(haybales)-min(haybales)]之间
 * 2. 对于每个可能的半径R，检查是否能引爆所有草包
 * 3. 贪心策略：从最左边开始，每次引爆能覆盖最远位置的草包
 * 
 * 时间复杂度: O(N * log(max-min))
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param haybales 草包位置数组
 * @param n 数组长度
 * @return 最小爆炸半径
 */
bool canCoverAll(int haybales[], int n, int radius) {
    int covered = 0;
    
    for (int i = 0; i < n; i++) {
        if (haybales[i] > covered) {
            // 需要引爆新的草包
            covered = haybales[i] + radius;
        } else if (haybales[i] + radius > covered) {
            // 更新覆盖范围
            covered = haybales[i] + radius;
        }
    }
    
    // 检查是否覆盖了所有草包
    return covered >= haybales[n - 1];
}

int angryCows(int haybales[], int n) {
    sort(haybales, haybales + n);
    
    int left = 0;
    int right = haybales[n - 1] - haybales[0];
    
    while (left < right) {
        int mid = left + ((right - left) >> 1);
        
        if (canCoverAll(haybales, n, mid)) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    
    return left;
}

/**
 * HackerRank: 最小化最大值
 * 题目来源: https://www.hackerrank.com/challenges/angry-children/problem
 * 
 * 题目描述:
 * 给定N个整数和一个整数K，从N个整数中选择K个数，使得这K个数中的最大值与最小值的差最小。
 * 
 * 思路分析:
 * 1. 先对数组排序
 * 2. 使用滑动窗口，检查每个长度为K的连续子数组
 * 3. 计算每个子数组的最大值与最小值的差（即最后一个元素与第一个元素的差）
 * 4. 返回最小差值
 * 
 * 时间复杂度: O(N log N)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param nums 整数数组
 * @param n 数组长度
 * @param K 选择的数字个数
 * @return 最小差值
 */
int minimizeMaximum(int nums[], int n, int K) {
    sort(nums, nums + n);
    
    int minDiff = nums[K - 1] - nums[0];
    
    for (int i = 1; i <= n - K; i++) {
        int diff = nums[i + K - 1] - nums[i];
        if (diff < minDiff) {
            minDiff = diff;
        }
    }
    
    return minDiff;
}

/**
 * 计蒜客: 跳石头
 * 题目来源: https://nanti.jisuanke.com/t/T1643
 * 
 * 题目描述:
 * 一条河上有一排石头，从起点到终点的距离是L。有N块石头在河中，位置分别为ai。
 * 可以移走M块石头，求移走M块石头后，最短跳跃距离的最大值。
 * 
 * 思路分析:
 * 1. 二分答案法，答案范围在[0, L]之间
 * 2. 对于每个可能的距离D，检查是否能通过移走<=M块石头实现最小跳跃距离>=D
 * 3. 贪心策略：从起点开始，每次跳到距离>=D的最近石头
 * 4. 如果必须移走的石头数>M，则不能实现；否则可以实现
 * 
 * 时间复杂度: O(N * log L)
 * 空间复杂度: O(1)
 * 是否最优解: 是
 * 
 * @param rocks 石头位置数组
 * @param n 石头数量
 * @param M 可以移走的石头数量
 * @param L 河的长度
 * @return 最短跳跃距离的最大值
 */
bool canAchieveMinDistance(int rocks[], int n, int M, int L, int minDist) {
    int removed = 0;
    int lastPos = 0;
    
    for (int i = 0; i < n; i++) {
        if (rocks[i] - lastPos < minDist) {
            // 必须移走这块石头
            removed++;
            if (removed > M) {
                return false;
            }
        } else {
            // 保留这块石头
            lastPos = rocks[i];
        }
    }
    
    // 检查终点距离
    if (L - lastPos < minDist) {
        removed++;
        if (removed > M) {
            return false;
        }
    }
    
    return true;
}

int jumpStones(int rocks[], int n, int M, int L) {
    sort(rocks, rocks + n);
    
    int left = 0;
    int right = L;
    
    while (left < right) {
        int mid = left + ((right - left + 1) >> 1);
        
        if (canAchieveMinDistance(rocks, n, M, L, mid)) {
            left = mid;
        } else {
            right = mid - 1;
        }
    }
    
    return left;
}

/**
 * Codeforces 474B - Worms
 * 题目来源: https://codeforces.com/problemset/problem/474/B
 * 
 * 题目描述:
 * 有N堆虫子，第i堆有ai只虫子。所有虫子按顺序编号从1开始。
 * 有M个查询，每个查询给出一个虫子编号，求这只虫子属于第几堆。
 * 
 * 思路分析:
 * 1. 先计算前缀和数组
 * 2. 对于每个查询，使用二分查找找到第一个>=查询编号的前缀和位置
 * 3. 该位置即为虫子所属的堆
 * 
 * 时间复杂度: O(N + M * log N)
 * 空间复杂度: O(N)
 * 是否最优解: 是
 * 
 * @param piles 每堆虫子数量数组
 * @param n 数组长度
 * @param queries 查询数组
 * @param m 查询数量
 * @param results 结果数组
 */
void findWormPiles(int piles[], int n, int queries[], int m, int results[]) {
    // 计算前缀和
    int* prefixSum = new int[n + 1];
    prefixSum[0] = 0;
    
    for (int i = 0; i < n; i++) {
        prefixSum[i + 1] = prefixSum[i] + piles[i];
    }
    
    // 处理每个查询
    for (int i = 0; i < m; i++) {
        int wormId = queries[i];
        
        // 二分查找第一个>=wormId的前缀和位置
        int left = 1, right = n;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (prefixSum[mid] >= wormId) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        results[i] = left;
    }
    
    delete[] prefixSum;
}
