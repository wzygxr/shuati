// 数列分块入门3 - Java实现
// 题目来源：LibreOJ #6279 数列分块入门3
// 题目链接：https://loj.ac/p/6279
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间加法，询问区间内小于某个值x的前驱（比其小的最大元素）
// 操作0：区间加法 [l, r] + c
// 操作1：询问区间内小于某个值x的前驱
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块维护一个TreeSet，用于快速查找前驱元素
// 3. 对于区间加法操作，不完整块直接更新并重新构建TreeSet，完整块使用懒惰标记
// 4. 对于查询操作，不完整块直接遍历，完整块使用TreeSet的lower方法优化
// 时间复杂度：预处理O(n log(√n))，区间加法操作O(√n * log(√n))，查询操作O(√n * log(√n))
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 4. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 5. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

package class172;

import java.io.*;
import java.util.*;

public class Code09_BlockIntro3_Java {
    
    // 最大数组大小
    public static final int MAXN = 500001;
    
    // 原数组
    public static int[] arr = new int[MAXN];
    
    // TreeSet用于维护每个块内的有序元素
    public static TreeSet<Integer>[] blockSets;
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个元素所属的块
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 每个块的懒惰标记（区间加法标记）
    public static int[] lazy = new int[MAXN];
    
    /**
     * 构建分块结构
     * 时间复杂度：O(n log(√n))
     * 空间复杂度：O(n)
     * @param n 数组长度
     */
    @SuppressWarnings("unchecked")
    public static void build(int n) {
        // 块大小取sqrt(n)
        blockSize = (int) Math.sqrt(n);
        // 块数量
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化TreeSet数组
        blockSets = new TreeSet[blockNum + 1];
        for (int i = 1; i <= blockNum; i++) {
            blockSets[i] = new TreeSet<>();
        }
        
        // 计算每个元素属于哪个块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 计算每个块的左右边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 将每个块内的元素添加到对应的TreeSet中
        for (int i = 1; i <= blockNum; i++) {
            blockSets[i].clear();
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                blockSets[i].add(arr[j]);
            }
        }
    }
    
    /**
     * 重构指定块的TreeSet
     * 当块内元素被修改后需要重新构建TreeSet
     * 时间复杂度：O(√n * log(√n))
     * @param blockId 块编号
     */
    public static void rebuild(int blockId) {
        blockSets[blockId].clear();
        for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
            blockSets[blockId].add(arr[i]);
        }
    }
    
    /**
     * 区间加法操作
     * 时间复杂度：O(√n * log(√n))
     * @param l 区间左端点
     * @param r 区间右端点
     * @param c 加的值
     */
    public static void add(int l, int r, int c) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                arr[i] += c;
            }
            // 重构该块的TreeSet
            rebuild(belongL);
        } else {
            // 处理左端不完整块
            for (int i = l; i <= blockRight[belongL]; i++) {
                arr[i] += c;
            }
            // 重构该块的TreeSet
            rebuild(belongL);
            
            // 处理右端不完整块
            for (int i = blockLeft[belongR]; i <= r; i++) {
                arr[i] += c;
            }
            // 重构该块的TreeSet
            rebuild(belongR);
            
            // 处理中间的完整块，使用懒惰标记
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                lazy[i] += c;
            }
        }
    }
    
    /**
     * 查询区间内小于v的最大元素（前驱）
     * 时间复杂度：O(√n * log(√n))
     * @param l 区间左端点
     * @param r 区间右端点
     * @param v 查找的值
     * @return 小于v的最大元素，不存在则返回-1
     */
    public static int query(int l, int r, int v) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        int result = -1;
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                int actualValue = arr[i] + lazy[belong[i]];
                if (actualValue < v) {
                    result = Math.max(result, actualValue);
                }
            }
        } else {
            // 处理左端不完整块
            for (int i = l; i <= blockRight[belongL]; i++) {
                int actualValue = arr[i] + lazy[belong[i]];
                if (actualValue < v) {
                    result = Math.max(result, actualValue);
                }
            }
            
            // 处理右端不完整块
            for (int i = blockLeft[belongR]; i <= r; i++) {
                int actualValue = arr[i] + lazy[belong[i]];
                if (actualValue < v) {
                    result = Math.max(result, actualValue);
                }
            }
            
            // 处理中间的完整块，使用TreeSet优化
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                // 调整v的值，考虑懒惰标记的影响
                int adjustedV = v - lazy[i];
                // 在TreeSet中查找小于adjustedV的最大元素
                Integer predecessor = blockSets[i].lower(adjustedV);
                if (predecessor != null) {
                    result = Math.max(result, predecessor + lazy[i]);
                }
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine());
        
        // 读取数组元素
        String[] elements = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(elements[i - 1]);
        }
        
        // 构建分块结构
        build(n);
        
        // 处理操作
        for (int i = 1; i <= n; i++) {
            String[] operation = reader.readLine().split(" ");
            int op = Integer.parseInt(operation[0]);
            int l = Integer.parseInt(operation[1]);
            int r = Integer.parseInt(operation[2]);
            
            if (op == 0) {
                // 区间加法操作
                int c = Integer.parseInt(operation[3]);
                add(l, r, c);
            } else {
                // 查询操作
                int v = Integer.parseInt(operation[3]);
                out.println(query(l, r, v));
            }
        }
        
        out.flush();
        out.close();
    }
}