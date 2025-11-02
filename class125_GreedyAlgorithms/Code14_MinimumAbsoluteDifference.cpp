// 最小绝对差
// 给你一个整数数组，其中数组中任意两个元素之间的绝对差的最小值。
// 测试链接 : https://www.hackerrank.com/challenges/minimum-absolute-difference-in-an-array/problem

/*
 * 算法思路：
 * 1. 贪心策略：排序后相邻元素的差值最小
 * 2. 将数组排序
 * 3. 遍历相邻元素，计算差值，找出最小值
 *
 * 时间复杂度：O(n * logn) - 主要是排序的时间复杂度
 * 空间复杂度：O(1) - 只使用了常数额外空间
 * 是否最优解：是，这是处理此类问题的最优解法
 *
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理空数组、单个元素等特殊情况
 * 3. 性能优化：一次遍历完成计算
 * 4. 可读性：清晰的变量命名和注释
 *
 * 极端场景与边界场景：
 * 1. 空输入：arr为空数组
 * 2. 极端值：只有一个元素、所有元素相同
 * 3. 重复数据：多个元素相同
 * 4. 有序/逆序数据：元素按顺序排列
 *
 * 跨语言场景与语言特性差异：
 * 1. Java：使用Arrays.sort进行排序
 * 2. C++：使用std::sort进行排序
 * 3. Python：使用sorted函数或list.sort()方法
 *
 * 调试能力构建：
 * 1. 打印中间过程：在循环中打印相邻元素和差值
 * 2. 用断言验证中间结果：确保差值不为负
 * 3. 性能退化排查：检查排序和遍历的时间复杂度
 *
 * 与机器学习、图像处理、自然语言处理的联系与应用：
 * 1. 在聚类算法中，可用于计算数据点之间的最小距离
 * 2. 在异常检测中，可用于识别异常值
 * 3. 在推荐系统中，可用于计算用户或物品之间的相似度
 */

// 简单的排序函数实现（冒泡排序）
void bubbleSort(int arr[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // 交换元素
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// 最小绝对差主函数
int minimumAbsoluteDifference(int arr[], int arrSize) {
    // 异常处理：检查输入是否为空
    if (arr == 0 || arrSize == 0) {
        return 0;
    }
    
    // 边界条件：只有一个元素
    if (arrSize == 1) {
        return 0;
    }
    
    // 使用冒泡排序对数组排序
    bubbleSort(arr, arrSize);
    
    // 初始化最小绝对差为最大值
    int minDiff = 2147483647;  // INT_MAX
    
    // 遍历相邻元素，计算差值，找出最小值
    for (int i = 1; i < arrSize; i++) {
        int diff = arr[i] - arr[i - 1];
        if (diff < minDiff) {
            minDiff = diff;
        }
    }
    
    return minDiff;
}