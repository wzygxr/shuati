// 数列分块入门5 - Java实现
// 题目来源：LibreOJ #6281 数列分块入门5
// 题目链接：https://loj.ac/p/6281
// 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间开方，区间求和
// 操作0：区间开方 [l, r] 每个元素开方后向下取整
// 操作1：区间求和 [l, r]
// 解题思路：
// 1. 使用分块算法，将数组分成sqrt(n)大小的块
// 2. 每个块维护元素和，用于快速计算区间和
// 3. 每个块维护一个标记，表示块中所有元素是否都变成了0或1（开方后不变）
// 4. 对于区间开方操作，如果块中所有元素都是0或1则无需处理，否则暴力处理
// 5. 对于查询操作，不完整块直接遍历，完整块直接使用块和计算
// 时间复杂度：预处理O(n)，区间开方操作O(√n)均摊，区间求和操作O(√n)
// 空间复杂度：O(n)
// 相关题目：
// 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
// 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
// 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
// 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
// 5. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
// 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
// 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
// 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

package class172;

import java.io.*;
import java.util.*;

public class Code11_BlockIntro5_Java {
    
    // 最大数组大小
    public static final int MAXN = 500001;
    
    // 原数组
    public static long[] arr = new long[MAXN];
    
    // 块大小和块数量
    public static int blockSize, blockNum;
    
    // 每个元素所属的块
    public static int[] belong = new int[MAXN];
    
    // 每个块的左右边界
    public static int[] blockLeft = new int[MAXN];
    public static int[] blockRight = new int[MAXN];
    
    // 每个块的元素和
    public static long[] sum = new long[MAXN];
    
    // 标记块中所有元素是否都变成了0或1
    public static boolean[] allZeroOrOne = new boolean[MAXN];
    
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
        
        // 计算每个元素属于哪个块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 计算每个块的左右边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = Math.min(i * blockSize, n);
        }
        
        // 计算每个块的元素和，并检查是否所有元素都是0或1
        for (int i = 1; i <= blockNum; i++) {
            sum[i] = 0;
            allZeroOrOne[i] = true;
            for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                sum[i] += arr[j];
                if (arr[j] != 0 && arr[j] != 1) {
                    allZeroOrOne[i] = false;
                }
            }
        }
    }
    
    /**
     * 对一个数进行开方操作
     * @param x 原数值
     * @return 开方后向下取整的结果
     */
    public static long sqrt(long x) {
        return (long) Math.sqrt(x);
    }
    
    /**
     * 区间开方操作
     * 时间复杂度：O(√n) 均摊
     * @param l 区间左端点
     * @param r 区间右端点
     */
    public static void sqrtOperation(int l, int r) {
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            // 如果该块所有元素都是0或1，则无需处理
            if (allZeroOrOne[belongL]) {
                return;
            }
            
            // 否则暴力处理
            for (int i = l; i <= r; i++) {
                sum[belongL] -= arr[i];
                arr[i] = sqrt(arr[i]);
                sum[belongL] += arr[i];
            }
            
            // 重新检查该块是否所有元素都是0或1
            allZeroOrOne[belongL] = true;
            for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
                if (arr[i] != 0 && arr[i] != 1) {
                    allZeroOrOne[belongL] = false;
                    break;
                }
            }
        } else {
            // 处理左端不完整块
            if (!allZeroOrOne[belongL]) {
                for (int i = l; i <= blockRight[belongL]; i++) {
                    sum[belongL] -= arr[i];
                    arr[i] = sqrt(arr[i]);
                    sum[belongL] += arr[i];
                }
                
                // 重新检查该块是否所有元素都是0或1
                allZeroOrOne[belongL] = true;
                for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
                    if (arr[i] != 0 && arr[i] != 1) {
                        allZeroOrOne[belongL] = false;
                        break;
                    }
                }
            }
            
            // 处理右端不完整块
            if (!allZeroOrOne[belongR]) {
                for (int i = blockLeft[belongR]; i <= r; i++) {
                    sum[belongR] -= arr[i];
                    arr[i] = sqrt(arr[i]);
                    sum[belongR] += arr[i];
                }
                
                // 重新检查该块是否所有元素都是0或1
                allZeroOrOne[belongR] = true;
                for (int i = blockLeft[belongR]; i <= blockRight[belongR]; i++) {
                    if (arr[i] != 0 && arr[i] != 1) {
                        allZeroOrOne[belongR] = false;
                        break;
                    }
                }
            }
            
            // 处理中间的完整块
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                // 如果该块所有元素都是0或1，则无需处理
                if (allZeroOrOne[i]) {
                    continue;
                }
                
                // 否则暴力处理
                for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                    sum[i] -= arr[j];
                    arr[j] = sqrt(arr[j]);
                    sum[i] += arr[j];
                }
                
                // 重新检查该块是否所有元素都是0或1
                allZeroOrOne[i] = true;
                for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
                    if (arr[j] != 0 && arr[j] != 1) {
                        allZeroOrOne[i] = false;
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 查询区间和
     * 时间复杂度：O(√n)
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间和
     */
    public static long query(int l, int r) {
        long result = 0;
        int belongL = belong[l];  // 左端点所属块
        int belongR = belong[r];  // 右端点所属块
        
        // 如果在同一个块内，直接暴力处理
        if (belongL == belongR) {
            for (int i = l; i <= r; i++) {
                result += arr[i];
            }
        } else {
            // 处理左端不完整块
            for (int i = l; i <= blockRight[belongL]; i++) {
                result += arr[i];
            }
            
            // 处理右端不完整块
            for (int i = blockLeft[belongR]; i <= r; i++) {
                result += arr[i];
            }
            
            // 处理中间的完整块，直接使用块的元素和
            for (int i = belongL + 1; i <= belongR - 1; i++) {
                result += sum[i];
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
            arr[i] = Long.parseLong(elements[i - 1]);
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
                // 区间开方操作
                sqrtOperation(l, r);
            } else {
                // 区间求和操作
                out.println(query(l, r));
            }
        }
        
        out.flush();
        out.close();
    }
}