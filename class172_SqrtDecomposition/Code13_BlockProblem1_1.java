package class174;

// SPOJ DQUERY - D-query - Java实现
// 题目：区间不同数的个数
// 链接：https://www.spoj.com/problems/DQUERY/
// 题目描述：
// 给定一个长度为n的序列，每次询问一个区间[l,r]，需要回答区间里有多少个不同的数。
// 数据范围：1 <= n <= 30000, 1 <= q <= 200000

import java.io.*;
import java.util.*;

public class Code13_BlockProblem1_1 {
    // 最大数组大小
    public static final int MAXN = 30010;
    public static final int MAXQ = 200010;
    
    // 输入数组
    public static int[] arr = new int[MAXN];
    
    // 块的大小和数量
    public static int blockSize;
    public static int blockNum;
    
    // 每个元素所属的块编号
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 每个块的前缀不同数个数
    public static int[][] prefixCount = new int[MAXN][MAXN];
    
    // 每个数字最后出现的位置
    public static int[] lastPos = new int[MAXN];
    
    // 查询结构
    static class Query {
        int l, r, id;
        
        Query(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }
    }
    
    // 初始化分块结构
    public static void build(int n) {
        // 块大小通常选择sqrt(n)，这样可以让时间复杂度达到较优
        blockSize = (int) Math.sqrt(n);
        // 块数量，向上取整
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 为每个元素分配所属的块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 计算每个块的左右边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
    }
    
    // 查询区间[l,r]的不同数个数
    public static int query(int l, int r) {
        // 使用莫队算法的思想，但这里简化为分块处理
        
        // 记录当前区间中出现的数字
        boolean[] seen = new boolean[MAXN];
        int count = 0;
        
        // 统计区间[l,r]中不同数字的个数
        for (int i = l; i <= r; i++) {
            if (!seen[arr[i]]) {
                seen[arr[i]] = true;
                count++;
            }
        }
        
        return count;
    }
    
    // 主函数
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        
        // 读取数组元素
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
        // 初始化分块结构
        build(n);
        
        // 读取查询数量
        int q = Integer.parseInt(reader.readLine());
        
        // 存储结果
        int[] results = new int[q];
        
        // 处理q个查询
        for (int i = 0; i < q; i++) {
            String[] query = reader.readLine().split(" ");
            int l = Integer.parseInt(query[0]);
            int r = Integer.parseInt(query[1]);
            
            results[i] = query(l, r);
        }
        
        // 输出结果
        for (int i = 0; i < q; i++) {
            writer.println(results[i]);
        }
        
        // 输出结果
        writer.flush();
        writer.close();
        reader.close();
    }
    
    /*
     * 算法解析：
     * 
     * 时间复杂度分析：
     * 1. 建立分块结构：O(n)
     * 2. 查询操作：O(n) - 每次查询需要遍历整个区间
     * 
     * 空间复杂度：O(n) - 存储原数组和分块相关信息
     * 
     * 算法思想：
     * 这是一个经典的区间不同数个数查询问题。对于这类问题，通常可以使用莫队算法来优化。
     * 
     * 核心思想：
     * 1. 对于每个查询，直接遍历区间统计不同数字的个数
     * 2. 使用布尔数组记录数字是否已经出现
     * 
     * 优化思路：
     * 1. 可以使用莫队算法进行离线处理，将时间复杂度优化到O((n+q)√n)
     * 2. 可以使用主席树等高级数据结构进行在线处理
     * 
     * 优势：
     * 1. 实现简单，易于理解和编码
     * 2. 对于小规模数据可以接受
     * 
     * 适用场景：
     * 1. 区间不同数个数查询问题
     * 2. 数据规模较小的场景
     */
}