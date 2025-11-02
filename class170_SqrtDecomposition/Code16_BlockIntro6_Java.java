// 数列分块入门6 - Java实现
// 题目来源：LibreOJ #6282 数列分块入门6
// 题目链接：https://loj.ac/p/6282
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及单点插入，单点查询
// 操作0：在位置x后插入一个数y
// 操作1：查询位置x的值
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块使用动态数组存储元素，支持快速插入和查询
// 3. 当某个块过大时（超过2*sqrt(n)），需要重构整个分块结构
// 4. 对于插入操作，在指定位置找到对应块并插入元素
// 5. 对于查询操作，遍历块找到指定位置的元素
// 时间复杂度：预处理O(n)，插入操作O(√n)均摊，查询操作O(√n)
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

package class172;

import java.io.*;
import java.util.*;

public class Code16_BlockIntro6_Java {
    
    // 最大数组大小
    public static final int MAXN = 1000005;
    
    // 原数组（使用动态数组实现）
    public static List<Integer> arr = new ArrayList<>();
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个块的左右边界
    public static List<List<Integer>> blocks = new ArrayList<>();
    
    /**
     * 构建分块结构
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * @param n 数组长度
     */
    public static void build(int n) {
        // 块大小取sqrt(n)
        blockSize = (int) Math.sqrt(n);
        // 块数量
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化块
        blocks.clear();
        for (int i = 0; i < blockNum; i++) {
            blocks.add(new ArrayList<>());
        }
        
        // 将元素分配到各个块中
        for (int i = 0; i < n; i++) {
            blocks.get(i / blockSize).add(arr.get(i));
        }
    }
    
    /**
     * 重构分块结构（当某个块过大时）
     * 时间复杂度：O(n)
     */
    public static void rebuild() {
        // 重新计算总元素数
        int total = 0;
        for (List<Integer> block : blocks) {
            total += block.size();
        }
        
        // 重新分配块大小
        blockSize = (int) Math.sqrt(total);
        blockNum = (total + blockSize - 1) / blockSize;
        
        // 重建数组
        arr.clear();
        for (List<Integer> block : blocks) {
            arr.addAll(block);
        }
        
        // 重新构建块结构
        blocks.clear();
        for (int i = 0; i < blockNum; i++) {
            blocks.add(new ArrayList<>());
        }
        
        for (int i = 0; i < arr.size(); i++) {
            blocks.get(i / blockSize).add(arr.get(i));
        }
    }
    
    /**
     * 单点插入操作
     * 时间复杂度：O(√n) 均摊
     * @param x 插入位置
     * @param y 插入的值
     */
    public static void insert(int x, int y) {
        // 找到x位置所在的块
        int blockIndex = 0;
        int count = 0;
        
        // 计算x位置在哪个块中
        for (int i = 0; i < blocks.size(); i++) {
            if (count + blocks.get(i).size() > x) {
                blockIndex = i;
                break;
            }
            count += blocks.get(i).size();
        }
        
        // 在对应块中插入元素
        int posInBlock = x - count;
        blocks.get(blockIndex).add(posInBlock, y);
        
        // 如果某个块过大，进行重构
        if (blocks.get(blockIndex).size() > 2 * blockSize) {
            rebuild();
        }
    }
    
    /**
     * 单点查询
     * 时间复杂度：O(√n)
     * @param x 查询位置
     * @return 位置x的值
     */
    public static int query(int x) {
        int count = 0;
        
        // 找到x位置所在的块
        for (List<Integer> block : blocks) {
            if (count + block.size() > x) {
                return block.get(x - count);
            }
            count += block.size();
        }
        
        return -1; // 位置不存在
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        
        // 读取数组元素
        String[] elements = reader.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            arr.add(Integer.parseInt(elements[i]));
        }
        
        // 构建分块结构
        build(n);
        
        // 处理操作
        for (int i = 0; i < n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int x = Integer.parseInt(operation[1]);
            
            if (op == 0) {
                // 单点插入操作
                int y = Integer.parseInt(operation[2]);
                insert(x, y);
            } else {
                // 单点查询操作
                out.println(query(x - 1)); // 转换为0索引
            }
        }
        
        out.flush();
        out.close();
    }
}