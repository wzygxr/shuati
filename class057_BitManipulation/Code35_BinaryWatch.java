package class031;

import java.util.*;

/**
 * 二进制手表
 * 测试链接：https://leetcode.cn/problems/binary-watch/
 * 
 * 题目描述：
 * 二进制手表顶部有 4 个 LED 代表 小时（0-11），底部的 6 个 LED 代表 分钟（0-59）。
 * 每个 LED 代表一个 0 或 1，最低位在右侧。
 * 给你一个整数 turnedOn ，表示当前亮着的 LED 的数量，返回所有可能的时间。
 * 你可以按任意顺序返回答案。
 * 
 * 示例：
 * 输入：turnedOn = 1
 * 输出：["0:01","0:02","0:04","0:08","0:16","0:32","1:00","2:00","4:00","8:00"]
 * 
 * 输入：turnedOn = 9
 * 输出：[]
 * 
 * 提示：
 * 0 <= turnedOn <= 10
 * 
 * 解题思路：
 * 1. 枚举法：枚举所有可能的小时和分钟组合
 * 2. 位运算法：利用Integer.bitCount()计算亮灯数量
 * 3. 回溯法：递归选择亮灯的位置
 * 
 * 时间复杂度分析：
 * - 枚举法：O(12 * 60) = O(720)，常数时间
 * - 位运算法：O(2^10) = O(1024)，常数时间
 * 
 * 空间复杂度分析：
 * - 枚举法：O(n)，n为结果数量
 * - 位运算法：O(n)，n为结果数量
 */
public class Code35_BinaryWatch {
    
    /**
     * 方法1：枚举法（推荐）
     * 枚举所有可能的小时和分钟，检查亮灯数量是否匹配
     * 时间复杂度：O(12 * 60) = O(720)
     * 空间复杂度：O(n)，n为结果数量
     */
    public List<String> readBinaryWatch1(int turnedOn) {
        List<String> result = new ArrayList<>();
        
        // 枚举所有可能的小时（0-11）和分钟（0-59）
        for (int hour = 0; hour < 12; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                // 计算小时和分钟的二进制中1的个数
                if (Integer.bitCount(hour) + Integer.bitCount(minute) == turnedOn) {
                    result.add(String.format("%d:%02d", hour, minute));
                }
            }
        }
        
        return result;
    }
    
    /**
     * 方法2：位运算法
     * 枚举所有10位二进制数（4位小时 + 6位分钟）
     * 时间复杂度：O(2^10) = O(1024)
     * 空间复杂度：O(n)，n为结果数量
     */
    public List<String> readBinaryWatch2(int turnedOn) {
        List<String> result = new ArrayList<>();
        
        // 枚举所有10位二进制数（0-1023）
        for (int i = 0; i < 1024; i++) {
            // 高4位表示小时，低6位表示分钟
            int hour = i >> 6;
            int minute = i & 0x3F; // 0x3F = 63，取低6位
            
            // 检查小时和分钟是否在有效范围内
            if (hour < 12 && minute < 60 && Integer.bitCount(i) == turnedOn) {
                result.add(String.format("%d:%02d", hour, minute));
            }
        }
        
        return result;
    }
    
    /**
     * 方法3：回溯法
     * 递归选择亮灯的位置
     * 时间复杂度：O(C(10, turnedOn))，组合数
     * 空间复杂度：O(n)，递归深度和结果数量
     */
    public List<String> readBinaryWatch3(int turnedOn) {
        List<String> result = new ArrayList<>();
        if (turnedOn < 0 || turnedOn > 10) {
            return result;
        }
        
        // 回溯选择亮灯位置
        backtrack(result, 0, 0, turnedOn, 0, 0);
        return result;
    }
    
    private void backtrack(List<String> result, int hour, int minute, 
                          int remaining, int start, int count) {
        // 如果剩余亮灯数为0，检查是否有效
        if (remaining == 0) {
            if (hour < 12 && minute < 60) {
                result.add(String.format("%d:%02d", hour, minute));
            }
            return;
        }
        
        // 从start位置开始选择亮灯
        for (int i = start; i < 10; i++) {
            if (count < remaining) {
                int newHour = hour;
                int newMinute = minute;
                
                // 根据位置设置小时或分钟
                if (i < 4) { // 小时位（0-3）
                    newHour = hour | (1 << (3 - i));
                } else { // 分钟位（4-9）
                    newMinute = minute | (1 << (9 - i));
                }
                
                backtrack(result, newHour, newMinute, remaining - 1, i + 1, count + 1);
            }
        }
    }
    
    /**
     * 方法4：预计算法
     * 预先计算所有可能的小时和分钟组合
     * 时间复杂度：O(1)，常数时间
     * 空间复杂度：O(1)，常数空间
     */
    public List<String> readBinaryWatch4(int turnedOn) {
        // 预计算所有可能的小时和分钟组合
        Map<Integer, List<String>> precomputed = new HashMap<>();
        
        // 枚举所有可能的小时和分钟
        for (int hour = 0; hour < 12; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                int count = Integer.bitCount(hour) + Integer.bitCount(minute);
                String time = String.format("%d:%02d", hour, minute);
                precomputed.computeIfAbsent(count, k -> new ArrayList<>()).add(time);
            }
        }
        
        return precomputed.getOrDefault(turnedOn, new ArrayList<>());
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code35_BinaryWatch solution = new Code35_BinaryWatch();
        
        // 测试用例1：亮1盏灯
        List<String> result1 = solution.readBinaryWatch1(1);
        System.out.println("测试用例1 - turnedOn = 1");
        System.out.println("结果数量: " + result1.size());
        System.out.println("前5个结果: " + result1.subList(0, Math.min(5, result1.size())));
        
        // 测试用例2：亮2盏灯
        List<String> result2 = solution.readBinaryWatch1(2);
        System.out.println("测试用例2 - turnedOn = 2");
        System.out.println("结果数量: " + result2.size());
        
        // 测试用例3：亮9盏灯（应该为空）
        List<String> result3 = solution.readBinaryWatch1(9);
        System.out.println("测试用例3 - turnedOn = 9");
        System.out.println("结果数量: " + result3.size());
        System.out.println("结果: " + result3);
        
        // 测试用例4：亮0盏灯
        List<String> result4 = solution.readBinaryWatch1(0);
        System.out.println("测试用例4 - turnedOn = 0");
        System.out.println("结果数量: " + result4.size());
        System.out.println("结果: " + result4);
        
        // 性能比较
        long startTime = System.currentTimeMillis();
        List<String> result5 = solution.readBinaryWatch1(3);
        long endTime = System.currentTimeMillis();
        System.out.println("方法1性能 - 耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        List<String> result6 = solution.readBinaryWatch2(3);
        endTime = System.currentTimeMillis();
        System.out.println("方法2性能 - 耗时: " + (endTime - startTime) + "ms");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 枚举法:");
        System.out.println("  时间复杂度: O(12 * 60) = O(720)");
        System.out.println("  空间复杂度: O(n)，n为结果数量");
        
        System.out.println("方法2 - 位运算法:");
        System.out.println("  时间复杂度: O(2^10) = O(1024)");
        System.out.println("  空间复杂度: O(n)，n为结果数量");
        
        System.out.println("方法3 - 回溯法:");
        System.out.println("  时间复杂度: O(C(10, turnedOn))");
        System.out.println("  空间复杂度: O(n)，递归深度和结果数量");
        
        System.out.println("方法4 - 预计算法:");
        System.out.println("  时间复杂度: O(1)");
        System.out.println("  空间复杂度: O(1)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 算法选择：方法1最简单实用");
        System.out.println("2. 边界处理：检查turnedOn范围（0-10）");
        System.out.println("3. 性能优化：720次枚举足够快");
        System.out.println("4. 可读性：方法1逻辑清晰易懂");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. Integer.bitCount(): 快速计算二进制中1的个数");
        System.out.println("2. 位运算：>> 和 & 操作提取高低位");
        System.out.println("3. 枚举法：当数据范围较小时，直接枚举所有可能");
        System.out.println("4. 格式化输出：String.format() 确保时间格式正确");
    }
}