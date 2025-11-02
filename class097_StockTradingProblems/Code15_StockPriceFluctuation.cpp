// LeetCode 2034. 股票价格波动
// 题目链接: https://leetcode.cn/problems/stock-price-fluctuation/
// 题目描述:
// 给你一支股票价格的波动序列，请你实现一个数据结构来处理这些波动。
// 实现 StockPrice 类：
// StockPrice() 初始化对象，当前无股票价格记录。
// void update(int timestamp, int price) 在时间点 timestamp 更新股票价格为 price 。
// int current() 返回股票最新价格。
// int maximum() 返回股票最高价格。
// int minimum() 返回股票最低价格。
//
// 解题思路:
// 这是一个设计类问题，需要高效地维护股票价格数据。
// 使用以下数据结构：
// 1. 数组存储时间戳到价格的映射
// 2. 有序数组维护价格的有序性
// 3. 维护最大时间戳以快速获取最新价格
//
// 算法步骤:
// 1. update操作：更新时间戳-价格映射，更新最大时间戳
// 2. current操作：直接返回最大时间戳对应的价格
// 3. maximum/minimum操作：遍历数组获取最值
//
// 时间复杂度分析:
// update: O(n)
// current: O(1)
// maximum/minimum: O(n)
//
// 空间复杂度分析:
// O(n) - 存储所有时间戳和价格

#define MAX_N 100000

// 定义StockPrice结构体
typedef struct {
    int timestamps[MAX_N];
    int prices[MAX_N];
    int size;
    int maxTimestamp;
    int maxTimestampPrice;
} StockPrice;

// 初始化对象
StockPrice* stockPriceCreate() {
    static StockPrice obj;
    obj.size = 0;
    obj.maxTimestamp = 0;
    return &obj;
}

// 在时间点 timestamp 更新股票价格为 price
void stockPriceUpdate(StockPrice* obj, int timestamp, int price) {
    // 查找是否已存在该时间戳
    int index = -1;
    for (int i = 0; i < obj->size; i++) {
        if (obj->timestamps[i] == timestamp) {
            index = i;
            break;
        }
    }
    
    if (index == -1) {
        // 新增时间戳
        obj->timestamps[obj->size] = timestamp;
        obj->prices[obj->size] = price;
        obj->size++;
    } else {
        // 更新已有时间戳的价格
        obj->prices[index] = price;
    }
    
    // 更新最大时间戳和对应价格
    if (timestamp >= obj->maxTimestamp) {
        obj->maxTimestamp = timestamp;
        obj->maxTimestampPrice = price;
    }
}

// 返回股票最新价格
int stockPriceCurrent(StockPrice* obj) {
    return obj->maxTimestampPrice;
}

// 返回股票最高价格
int stockPriceMaximum(StockPrice* obj) {
    int maxPrice = obj->prices[0];
    for (int i = 1; i < obj->size; i++) {
        if (obj->prices[i] > maxPrice) {
            maxPrice = obj->prices[i];
        }
    }
    return maxPrice;
}

// 返回股票最低价格
int stockPriceMinimum(StockPrice* obj) {
    int minPrice = obj->prices[0];
    for (int i = 1; i < obj->size; i++) {
        if (obj->prices[i] < minPrice) {
            minPrice = obj->prices[i];
        }
    }
    return minPrice;
}

// 由于编译环境限制，不提供测试函数
// 可以通过LeetCode平台进行测试