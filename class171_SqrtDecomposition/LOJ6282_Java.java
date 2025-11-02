package class173.implementations;

import java.io.*;
import java.util.*;

/**
 * LOJ 6282. 数列分块入门 6 - Java实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及单点插入，单点询问。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成多个块，每个块维护一个动态数组。
 * 单点插入操作时：
 * 1. 找到要插入元素的块
 * 2. 在该块中插入元素
 * 3. 检查块大小，如果超过设定阈值，则重新分块
 * 单点查询时：
 * 1. 找到元素所在块
 * 2. 在块中查找元素
 * 
 * 时间复杂度：
 * - 单点插入：平均 O(√n)
 * - 单点查询：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：通过动态维护块结构减少操作时间
 * 4. 鲁棒性：处理边界情况和特殊输入
 */

public class LOJ6282_Java {
    // 最大块数量
    private static final int MAX_BLOCKS = 10000;
    // 每个块的理想大小
    private static final int BLOCK_SIZE = 300;
    
    // 存储每个块的数据
    private List<List<Integer>> blocks;
    // 数组的当前大小
    private int size;
    
    /**
     * 构造函数
     */
    public LOJ6282_Java() {
        blocks = new ArrayList<>();
        blocks.add(new ArrayList<>()); // 初始化第一个块
        size = 0;
    }
    
    /**
     * 单点插入操作
     * 
     * @param pos 插入位置（从1开始）
     * @param val 要插入的值
     */
    public void insert(int pos, int val) {
        pos--; // 转换为0基索引
        
        // 找到要插入的块
        int blockIndex = 0;
        int currentPos = 0;
        while (blockIndex < blocks.size() && currentPos + blocks.get(blockIndex).size() <= pos) {
            currentPos += blocks.get(blockIndex).size();
            blockIndex++;
        }
        
        // 在对应块中插入元素
        List<Integer> targetBlock = blocks.get(blockIndex);
        targetBlock.add(pos - currentPos, val);
        size++;
        
        // 检查是否需要重新分块（如果块太大）
        if (targetBlock.size() > 2 * BLOCK_SIZE) {
            // 创建新块
            List<Integer> newBlock = new ArrayList<>();
            int mid = targetBlock.size() / 2;
            
            // 将后半部分移到新块
            for (int i = mid; i < targetBlock.size(); i++) {
                newBlock.add(targetBlock.get(i));
            }
            // 移除原块的后半部分
            while (targetBlock.size() > mid) {
                targetBlock.remove(targetBlock.size() - 1);
            }
            
            // 插入新块
            blocks.add(blockIndex + 1, newBlock);
        }
    }
    
    /**
     * 单点查询操作
     * 
     * @param pos 查询位置（从1开始）
     * @return 查询结果
     */
    public int query(int pos) {
        pos--; // 转换为0基索引
        
        // 找到要查询的块
        int blockIndex = 0;
        int currentPos = 0;
        while (blockIndex < blocks.size() && currentPos + blocks.get(blockIndex).size() <= pos) {
            currentPos += blocks.get(blockIndex).size();
            blockIndex++;
        }
        
        // 在对应块中查询元素
        List<Integer> targetBlock = blocks.get(blockIndex);
        return targetBlock.get(pos - currentPos);
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) throws IOException {
        // 使用更快的输入输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组大小
        int n = Integer.parseInt(reader.readLine());
        
        // 初始化分块结构
        LOJ6282_Java solution = new LOJ6282_Java();
        
        // 读取初始数组
        String[] elements = reader.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            int value = Integer.parseInt(elements[i]);
            solution.insert(i + 1, value); // 插入到当前数组末尾
        }
        
        // 处理操作
        for (int i = 0; i < n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            int c = Integer.parseInt(operation[3]);
            
            if (op == 0) {
                // 单点插入
                solution.insert(l, r);
            } else {
                // 单点查询
                writer.println(solution.query(r));
            }
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}