// Codeforces 865D. Buy Low Sell High
// 题目链接: https://codeforces.com/problemset/problem/865/D
// 题目描述:
// 有n天，每天股票的价格是ai。每天你可以选择买入一股、卖出一股或什么都不做。
// 你不能同时拥有超过一股股票。在第n天结束时，你不能持有任何股票。
// 求最大利润。
//
// 解题思路:
// 这是一个经典的反悔贪心问题。
// 使用数组模拟优先队列（最小堆）来维护所有买入的股票价格。
// 算法步骤：
// 1. 遍历每一天的价格
// 2. 将当前价格加入数组
// 3. 对数组进行排序
// 4. 如果最小元素小于当前价格，则计算利润
//
// 时间复杂度分析:
// O(n² log n) - 每次插入后都需要排序
//
// 空间复杂度分析:
// O(n) - 数组的空间

#define MAX_N 100000

// 计算最大利润
// 参数说明:
// prices: 股票价格数组
// n: 数组长度
// 返回值: 最大利润
long long maxProfit(int prices[], int n) {
    // 边界条件处理
    if (n == 0) {
        return 0;
    }
    
    // 使用数组维护买入的股票价格
    int heap[MAX_N];
    int heapSize = 0;
    
    // 总利润
    long long totalProfit = 0;
    
    // 遍历每一天的价格
    for (int i = 0; i < n; i++) {
        int price = prices[i];
        
        // 将当前价格加入数组
        heap[heapSize++] = price;
        
        // 对数组进行排序（模拟最小堆）
        for (int j = 0; j < heapSize - 1; j++) {
            for (int k = j + 1; k < heapSize; k++) {
                if (heap[j] > heap[k]) {
                    int temp = heap[j];
                    heap[j] = heap[k];
                    heap[k] = temp;
                }
            }
        }
        
        // 如果数组中有至少两个元素且最小元素小于当前价格
        // 则可以进行交易获得利润
        if (heapSize >= 2 && heap[0] < price) {
            // 取出最小价格（买入）
            int buyPrice = heap[0];
            // 移除最小价格
            for (int j = 0; j < heapSize - 1; j++) {
                heap[j] = heap[j + 1];
            }
            heapSize--;
            // 计算利润
            totalProfit += price - buyPrice;
            // 将当前价格再次加入数组（表示反悔机制）
            heap[heapSize++] = price;
        }
    }
    
    return totalProfit;
}

// 由于编译环境限制，不提供测试函数
// 可以通过Codeforces平台进行测试