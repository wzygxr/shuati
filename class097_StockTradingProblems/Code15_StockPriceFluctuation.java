package class082;

import java.util.*;

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
// 1. HashMap 存储时间戳到价格的映射
// 2. TreeMap 或 PriorityQueue 维护价格的有序性
// 3. 维护最大时间戳以快速获取最新价格
//
// 算法步骤:
// 1. update操作：更新时间戳-价格映射，更新最大时间戳
// 2. current操作：直接返回最大时间戳对应的价格
// 3. maximum/minimum操作：使用有序数据结构快速获取最值
//
// 时间复杂度分析:
// update: O(log n)
// current: O(1)
// maximum/minimum: O(1) 或 O(log n)
//
// 空间复杂度分析:
// O(n) - 存储所有时间戳和价格

public class Code15_StockPriceFluctuation {
    // 存储时间戳到价格的映射
    private Map<Integer, Integer> timestampToPrice;
    
    // 使用TreeMap维护价格计数，key为价格，value为该价格出现的次数
    private TreeMap<Integer, Integer> priceCount;
    
    // 记录最大时间戳
    private int maxTimestamp;
    
    /**
     * 初始化对象
     */
    public Code15_StockPriceFluctuation() {
        timestampToPrice = new HashMap<>();
        priceCount = new TreeMap<>();
        maxTimestamp = 0;
    }
    
    /**
     * 在时间点 timestamp 更新股票价格为 price
     * @param timestamp 时间戳
     * @param price 价格
     */
    public void update(int timestamp, int price) {
        // 如果该时间戳之前已经有价格，需要先从priceCount中移除旧价格
        if (timestampToPrice.containsKey(timestamp)) {
            int oldPrice = timestampToPrice.get(timestamp);
            // 减少旧价格的计数
            priceCount.put(oldPrice, priceCount.get(oldPrice) - 1);
            // 如果计数为0，移除该价格
            if (priceCount.get(oldPrice) == 0) {
                priceCount.remove(oldPrice);
            }
        }
        
        // 更新时间戳到价格的映射
        timestampToPrice.put(timestamp, price);
        
        // 更新价格计数
        priceCount.put(price, priceCount.getOrDefault(price, 0) + 1);
        
        // 更新最大时间戳
        maxTimestamp = Math.max(maxTimestamp, timestamp);
    }
    
    /**
     * 返回股票最新价格
     * @return 最新价格
     */
    public int current() {
        return timestampToPrice.get(maxTimestamp);
    }
    
    /**
     * 返回股票最高价格
     * @return 最高价格
     */
    public int maximum() {
        return priceCount.lastKey();
    }
    
    /**
     * 返回股票最低价格
     * @return 最低价格
     */
    public int minimum() {
        return priceCount.firstKey();
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code15_StockPriceFluctuation stockPrice = new Code15_StockPriceFluctuation();
        
        // 测试用例
        stockPrice.update(1, 10);
        stockPrice.update(2, 5);
        
        assert stockPrice.current() == 5 : "current()测试失败";
        assert stockPrice.maximum() == 10 : "maximum()测试失败";
        assert stockPrice.minimum() == 5 : "minimum()测试失败";
        
        stockPrice.update(1, 3);  // 更新时间戳1的价格为3
        
        assert stockPrice.maximum() == 5 : "maximum()测试失败";
        assert stockPrice.minimum() == 3 : "minimum()测试失败";
        
        stockPrice.update(4, 2);  // 添加时间戳4，价格为2
        
        assert stockPrice.minimum() == 2 : "minimum()测试失败";
        
        System.out.println("所有测试通过!");
    }
}