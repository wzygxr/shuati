// 数列分块入门9 - Java实现
// 题目来源：LibreOJ #6285 数列分块入门9
// 题目链接：https://loj.ac/p/6285
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及询问区间的最小众数
// 操作：询问区间[l,r]的最小众数（出现次数最多，相同出现次数时取最小值）
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 预处理每个块区间[i,j]的最小众数，存储在f[i][j]中
// 3. 对于每个块，维护其中每个值的出现次数
// 4. 对于查询操作，如果区间跨越多个块，则利用预处理结果和暴力统计边界块
// 5. 最小众数定义：出现次数最多，相同出现次数时取最小值
// 时间复杂度：预处理O(n√n)，查询操作O(√n)
// 空间复杂度：O(n + √n * √n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
// 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 7. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 8. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284

package class172;

import java.io.*;
import java.util.*;

public class Code15_BlockIntro9_Java {
    
    // 最大数组大小
    public static final int MAXN = 100001;
    
    // 原数组
    public static int[] arr = new int[MAXN];
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个元素所属的块
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 预处理：f[i][j] 表示第i块到第j块的最小众数
    public static int[][] f = new int[1001][1001];
    
    // 每个值在每个块中的出现次数
    public static Map<Integer, Integer>[] countInBlock;
    
    /**
     * 构建分块结构
     * 时间复杂度：O(n√n)
     * 空间复杂度：O(n + √n * √n)
     * @param n 数组长度
     */
    @SuppressWarnings("unchecked")
    public static void build(int n) {
        // 块大小取sqrt(n)
        blockSize = (int) Math.sqrt(n);
        // 块数量
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 计算每个元素属于哪个块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 计算每个块的左右边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 初始化countInBlock数组
        countInBlock = new HashMap[blockNum + 1];
        for (int i = 1; i <= blockNum; i++) {
            countInBlock[i] = new HashMap<>();
        }
        
        // 计算每个块中每个值的出现次数
        for (int i = 1; i <= blockNum; i++) {
            countInBlock[i].clear();
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                countInBlock[i].put(arr[j], countInBlock[i].getOrDefault(arr[j], 0) + 1);
            }
        }
        
        // 预处理f数组
        for (int i = 1; i <= blockNum; i++) {
            Map<Integer, Integer> totalCount = new HashMap<>();
            for (int j = i; j <= blockNum; j++) {
                // 将第j块的计数加入总统计
                for (Map.Entry<Integer, Integer> entry : countInBlock[j].entrySet()) {
                    int value = entry.getKey();
                    int count = entry.getValue();
                    totalCount.put(value, totalCount.getOrDefault(value, 0) + count);
                }
                
                // 找到当前范围的最小众数
                int mode = Integer.MAX_VALUE;
                int maxCount = 0;
                for (Map.Entry<Integer, Integer> entry : totalCount.entrySet()) {
                    int value = entry.getKey();
                    int count = entry.getValue();
                    if (count > maxCount || (count == maxCount && value < mode)) {
                        maxCount = count;
                        mode = value;
                    }
                }
                f[i][j] = mode;
            }
        }
    }
    
    /**
     * 计算区间[l,r]内值c的出现次数
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @param c 要统计的值
     * @return 值c在区间[l,r]内的出现次数
     */
    public static int countInRange(int l, int r, int c) {
        int result = 0;
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力统计
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                if (arr[i] == c) {
                    result++;
                }
            }
        } else {
            // 处理左端不完整块
            for (int i = l; i <= blockRight[belongL]; i++) {
                if (arr[i] == c) {
                    result++;
                }
            }
            
            // 处理右端不完整块
            for (int i = blockLeft[belongR]; i <= r; i++) {
                if (arr[i] == c) {
                    result++;
                }
            }
            
            // 处理中间的完整块
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                result += countInBlock[i].getOrDefault(c, 0);
            }
        }
        
        return result;
    }
    
    /**
     * 查询区间[l,r]的最小众数
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间[l,r]的最小众数
     */
    public static int query(int l, int r) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            Map<Integer, Integer> count = new HashMap<>();
            for (int i = l; i <= r; i++) {
                count.put(arr[i], count.getOrDefault(arr[i], 0) + 1);
            }
            
            int mode = Integer.MAX_VALUE;
            int maxCount = 0;
            for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
                int value = entry.getKey();
                int cnt = entry.getValue();
                if (cnt > maxCount || (cnt == maxCount && value < mode)) {
                    maxCount = cnt;
                    mode = value;
                }
            }
            return mode;
        } else {
            // 获取中间完整块的最小众数
            int mode = f[belongL + 1][belongR - 1];
            int maxCount = countInRange(l, r, mode);
            
            // 检查左端不完整块中的值
            for (int i = l; i <= blockRight[belongL]; i++) {
                int value = arr[i];
                int cnt = countInRange(l, r, value);
                if (cnt > maxCount || (cnt == maxCount && value < mode)) {
                    maxCount = cnt;
                    mode = value;
                }
            }
            
            // 检查右端不完整块中的值
            for (int i = blockLeft[belongR]; i <= r; i++) {
                int value = arr[i];
                int cnt = countInRange(l, r, value);
                if (cnt > maxCount || (cnt == maxCount && value < mode)) {
                    maxCount = cnt;
                    mode = value;
                }
            }
            
            return mode;
        }
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
            int l = Integer.parseInt(operation[0]);
            int r = Integer.parseInt(operation[1]);
            
            // 区间查询最小众数
            out.println(query(l, r));
        }
        
        out.flush();
        out.close();
    }
}