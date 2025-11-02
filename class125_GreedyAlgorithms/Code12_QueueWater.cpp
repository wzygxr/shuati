// 排队接水
// 有 n 个人在一个水龙头前排队接水，假如每个人接水的时间为 Ti，
// 请编程找出这 n 个人排队的一种顺序，使得 n 个人的平均等待时间最小。
// 一个人的等待时间不包括他的接水时间。
// 如果两个人接水的时间相同，编号更小的人应当排在前面。
// 测试链接 : https://www.luogu.com.cn/problem/P1223

/*
 * 算法思路：
 * 1. 贪心策略：按接水时间升序排列
 * 2. 接水时间短的人排在前面，可以减少后面人的等待时间
 * 3. 计算排列后的平均等待时间
 *
 * 时间复杂度：O(n * logn) - 主要是排序的时间复杂度
 * 空间复杂度：O(n) - 存储排序后的索引
 * 是否最优解：是，这是处理此类问题的最优解法
 *
 * 工程化考量：
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理空数组、单个元素等特殊情况
 * 3. 性能优化：使用贪心策略避免穷举
 * 4. 可读性：清晰的变量命名和注释
 *
 * 极端场景与边界场景：
 * 1. 空输入：times为空数组
 * 2. 极端值：只有一人、所有人的接水时间相同
 * 3. 重复数据：多人接水时间相同
 * 4. 有序/逆序数据：接水时间按顺序排列
 *
 * 跨语言场景与语言特性差异：
 * 1. Java：使用Arrays.sort进行排序
 * 2. C++：使用std::sort进行排序
 * 3. Python：使用sorted函数或list.sort()方法
 *
 * 调试能力构建：
 * 1. 打印中间过程：在循环中打印当前排列顺序和等待时间
 * 2. 用断言验证中间结果：确保排列后等待时间最小
 * 3. 性能退化排查：检查排序和遍历的时间复杂度
 *
 * 与机器学习、图像处理、自然语言处理的联系与应用：
 * 1. 在任务调度问题中，贪心算法可用于优化平均等待时间
 * 2. 在操作系统中，可用于进程调度算法设计
 * 3. 在网络通信中，可用于数据包调度优化
 */

// 简单的排序函数实现（冒泡排序）
void bubbleSort(int times[], int ids[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            // 按接水时间升序排序，时间相同时按编号升序排序
            if (times[j] > times[j + 1] || (times[j] == times[j + 1] && ids[j] > ids[j + 1])) {
                // 交换时间
                int tempTime = times[j];
                times[j] = times[j + 1];
                times[j + 1] = tempTime;
                
                // 交换编号
                int tempId = ids[j];
                ids[j] = ids[j + 1];
                ids[j + 1] = tempId;
            }
        }
    }
}

// 排队接水主函数
double queueWater(int times[], int n, int result[]) {
    // 异常处理：检查输入是否为空
    if (times == 0 || n == 0) {
        result[0] = 0;
        return 0.0;
    }
    
    // 边界条件：只有一个人
    if (n == 1) {
        result[0] = 1;
        result[1] = 0;
        return 0.0;
    }
    
    // 创建编号数组
    int ids[1000];  // 假设最多1000人
    for (int i = 0; i < n; i++) {
        ids[i] = i + 1;  // 编号从1开始
    }
    
    // 按接水时间升序排序，时间相同时按编号升序排序
    bubbleSort(times, ids, n);
    
    // 计算平均等待时间
    long long totalWaitTime = 0;
    long long waitTime = 0;
    
    // 计算每个人的等待时间
    for (int i = 0; i < n - 1; i++) {  // 最后一个人没有等待时间
        waitTime += times[i];
        totalWaitTime += waitTime;
    }
    
    // 返回排列顺序
    for (int i = 0; i < n; i++) {
        result[i] = ids[i];
    }
    
    // 返回平均等待时间
    return (double) totalWaitTime / n;
}