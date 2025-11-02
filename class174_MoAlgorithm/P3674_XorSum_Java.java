package class176;

// 小清新人渣的本愿 (普通莫队应用 - Bitset优化)
// 题目来源: 洛谷P3674 小清新人渣的本愿
// 题目链接: https://www.luogu.com.cn/problem/P3674
// 题意: 给你一个序列 a，长度为 n，有 m 次操作，每次询问一个区间是否可以选出两个数它们的差为 x，或者询问一个区间是否可以选出两个数它们的和为 x，或者询问一个区间是否可以选出两个数它们的乘积为 x ，这三个操作分别为操作 1,2,3。
// 算法思路: 使用普通莫队算法，结合bitset优化区间查询
// 时间复杂度: O((n + m) * sqrt(n) * c / 32)，其中c为值域大小
// 空间复杂度: O(n + c)
// 适用场景: 区间和、差、积查询问题

import java.io.*;
import java.util.*;
import java.math.BigInteger;

public class P3674_XorSum_Java {
    
    static class Query {
        int l, r, x, opt, id;
        
        Query(int l, int r, int x, int opt, int id) {
            this.l = l;
            this.r = r;
            this.x = x;
            this.opt = opt;
            this.id = id;
        }
    }
    
    static class MoAlgorithm {
        static final int MAXN = 100001;
        static final int MAXV = 100001;
        
        int[] arr = new int[MAXN];
        int[] block = new int[MAXN];
        int[] cnt = new int[MAXV]; // 记录每个值出现的次数
        java.util.BitSet bitset = new java.util.BitSet(MAXV); // 优化查询
        int blockSize;
        boolean[] results;
        
        // 添加元素
        void add(int pos) {
            int val = arr[pos];
            if (cnt[val] == 0) {
                bitset.set(val);
            }
            cnt[val]++;
        }
        
        // 删除元素
        void remove(int pos) {
            int val = arr[pos];
            cnt[val]--;
            if (cnt[val] == 0) {
                bitset.clear(val);
            }
        }
        
        // 检查是否存在两个数的差为x
        boolean checkDifference(int x) {
            // 检查是否存在 a - b = x，即 a = b + x
            // 遍历所有可能的b值，检查b+x是否存在
            for (int b = 0; b + x < MAXV; b++) {
                if (bitset.get(b) && bitset.get(b + x)) {
                    // 特殊情况：x=0时需要至少有两个相同的数
                    if (x == 0 && cnt[b] >= 2) {
                        return true;
                    } else if (x != 0) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        // 检查是否存在两个数的和为x
        boolean checkSum(int x) {
            // 检查是否存在 a + b = x，即 b = x - a
            // 遍历所有可能的a值，检查x-a是否存在
            for (int a = 0; a <= x && a < MAXV; a++) {
                if (bitset.get(a) && bitset.get(x - a)) {
                    // 特殊情况：a = x-a时需要至少有两个相同的数
                    if (a == x - a && cnt[a] >= 2) {
                        return true;
                    } else if (a != x - a) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        // 检查是否存在两个数的乘积为x
        boolean checkProduct(int x) {
            // 检查是否存在 a * b = x
            // 遍历所有可能的a值，检查x/a是否为整数且存在
            for (int a = 1; a * a <= x && a < MAXV; a++) {
                if (x % a == 0) {
                    int b = x / a;
                    if (b < MAXV && bitset.get(a) && bitset.get(b)) {
                        // 特殊情况：a = b时需要至少有两个相同的数
                        if (a == b && cnt[a] >= 2) {
                            return true;
                        } else if (a != b) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        
        // 处理查询
        boolean[] processQueries(int n, int[][] queries) {
            int m = queries.length;
            Query[] queryList = new Query[m];
            results = new boolean[m];
            
            // 初始化块大小
            blockSize = (int) Math.sqrt(n);
            
            // 为每个位置分配块
            for (int i = 1; i <= n; i++) {
                block[i] = (i - 1) / blockSize + 1;
            }
            
            // 创建查询列表
            for (int i = 0; i < m; i++) {
                queryList[i] = new Query(queries[i][0], queries[i][1], queries[i][2], queries[i][3], i);
            }
            
            // 按照莫队算法排序
            Arrays.sort(queryList, new Comparator<Query>() {
                public int compare(Query a, Query b) {
                    if (block[a.l] != block[b.l]) {
                        return block[a.l] - block[b.l];
                    }
                    return a.r - b.r;
                }
            });
            
            int curL = 1, curR = 0;
            
            // 处理每个查询
            for (int i = 0; i < m; i++) {
                int L = queryList[i].l;
                int R = queryList[i].r;
                int x = queryList[i].x;
                int opt = queryList[i].opt;
                int idx = queryList[i].id;
                
                // 扩展右边界
                while (curR < R) {
                    curR++;
                    add(curR);
                }
                
                // 收缩右边界
                while (curR > R) {
                    remove(curR);
                    curR--;
                }
                
                // 收缩左边界
                while (curL < L) {
                    remove(curL);
                    curL++;
                }
                
                // 扩展左边界
                while (curL > L) {
                    curL--;
                    add(curL);
                }
                
                // 根据操作类型检查结果
                switch (opt) {
                    case 1: // 差
                        results[idx] = checkDifference(x);
                        break;
                    case 2: // 和
                        results[idx] = checkSum(x);
                        break;
                    case 3: // 积
                        results[idx] = checkProduct(x);
                        break;
                    default:
                        results[idx] = false;
                }
            }
            
            return results;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        
        MoAlgorithm mo = new MoAlgorithm();
        
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            mo.arr[i] = Integer.parseInt(parts[i - 1]);
        }
        
        int[][] queries = new int[m][4]; // l, r, x, opt
        
        for (int i = 0; i < m; i++) {
            parts = br.readLine().split(" ");
            int opt = Integer.parseInt(parts[0]);
            int l = Integer.parseInt(parts[1]);
            int r = Integer.parseInt(parts[2]);
            int x = Integer.parseInt(parts[3]);
            queries[i][0] = l;
            queries[i][1] = r;
            queries[i][2] = x;
            queries[i][3] = opt;
        }
        
        boolean[] results = mo.processQueries(n, queries);
        
        for (boolean result : results) {
            out.println(result ? "hana" : "bi");
        }
        
        out.flush();
    }
}