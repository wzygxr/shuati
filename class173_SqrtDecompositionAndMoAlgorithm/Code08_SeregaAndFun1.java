package class175;

// Serega and Fun问题 - 分块算法实现 (Java版本)
// 题目来源: https://codeforces.com/problemset/problem/455/D
// 题目大意: 给定一个长度为n的数组arr，有q次操作，操作分为两种类型：
// 类型1: l r - 计算区间[l,r]内值为k的元素个数，其中k是该区间内出现次数最多的数字
// 类型2: l r - 将arr[l]移动到位置r
// 约束条件: 
// 1 <= n, q <= 10^5
// 1 <= arr[i] <= n

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class Code08_SeregaAndFun1 {

    // 定义最大数组长度
    public static int MAXN = 100001;
    
    // n: 数组长度, q: 操作次数, blen: 块大小
    public static int n, q, blen;
    
    // 使用分块，每个块用LinkedList存储元素
    public static ArrayList<LinkedList<Integer>> blocks = new ArrayList<>();
    
    // 每个块中各个数值的出现次数
    public static ArrayList<int[]> cnt = new ArrayList<>();
    
    // 块的数量
    public static int bcnt;

    /**
     * 重构分块结构
     * 当块的数量远超sqrt(n)时，重新分块以保持效率
     */
    public static void reBuild() {
        // 当块的数量远超sqrt(n)时，重新分块
        if (bcnt > 2 * blen) {
            // 将所有元素收集到一个临时列表中
            ArrayList<Integer> tmp = new ArrayList<>();
            for (int i = 0; i < bcnt; i++) {
                tmp.addAll(blocks.get(i));
            }
            
            // 清空原有的分块结构
            blocks.clear();
            cnt.clear();
            bcnt = 0;
            
            // 重新分块
            for (int i = 0; i < tmp.size(); i++) {
                // 每blen个元素作为一个块
                if (i % blen == 0) {
                    blocks.add(new LinkedList<>());
                    cnt.add(new int[MAXN]);
                    bcnt++;
                }
                // 将元素添加到对应的块中
                int val = tmp.get(i);
                blocks.get(bcnt - 1).add(val);
                cnt.get(bcnt - 1)[val]++;
            }
        }
    }

    /**
     * 查询区间[l,r]内出现最多的数字的出现次数
     * @param l 左边界(1-indexed)
     * @param r 右边界(1-indexed)
     * @return 出现最多的数字的出现次数
     */
    public static int query(int l, int r) {
        // 转换为0-indexed
        l--;
        r--;
        
        // 计算左右边界所在的块
        int lb = l / blen;
        int rb = r / blen;
        int ans = 0;

        // 如果在同一个块内
        if (lb == rb) {
            // 直接遍历计算
            int[] count = new int[MAXN];
            for (int i = l; i <= r; i++) {
                int blockId = i / blen;
                int indexInBlock = i % blen;
                int val = (int) blocks.get(blockId).get(indexInBlock);
                count[val]++;
                ans = Math.max(ans, count[val]);
            }
        } else {
            // 跨多个块
            int[] count = new int[MAXN];
            
            // 处理左端不完整块
            int lEnd = (lb + 1) * blen - 1;
            for (int i = l; i <= lEnd; i++) {
                int blockId = i / blen;
                int indexInBlock = i % blen;
                int val = (int) blocks.get(blockId).get(indexInBlock);
                count[val]++;
                ans = Math.max(ans, count[val]);
            }

            // 处理中间完整块
            for (int i = lb + 1; i <= rb - 1; i++) {
                for (int j = 1; j < MAXN; j++) {
                    count[j] += cnt.get(i)[j];
                    ans = Math.max(ans, count[j]);
                }
            }

            // 处理右端不完整块
            int rStart = rb * blen;
            for (int i = rStart; i <= r; i++) {
                int blockId = i / blen;
                int indexInBlock = i % blen;
                int val = (int) blocks.get(blockId).get(indexInBlock);
                count[val]++;
                ans = Math.max(ans, count[val]);
            }
        }

        return ans;
    }

    /**
     * 将位置l的元素移动到位置r
     * @param l 源位置(1-indexed)
     * @param r 目标位置(1-indexed)
     */
    public static void move(int l, int r) {
        // 转换为0-indexed
        l--;
        r--;
        
        // 计算左右位置所在的块
        int lb = l / blen;
        int rb = r / blen;
        int lIndexInBlock = l % blen;
        
        // 从源块中移除元素
        int val = (int) blocks.get(lb).remove(lIndexInBlock);
        cnt.get(lb)[val]--;
        
        // 计算在新位置的索引
        int newIndexInBlock = r % blen;
        if (lb < rb) {
            // 如果从前面的块移动到后面的块
            newIndexInBlock = newIndexInBlock - (lb + 1) * blen + (lb * blen) + blocks.get(lb).size();
        } else if (lb > rb) {
            // 如果从后面的块移动到前面的块
            newIndexInBlock = newIndexInBlock + (lb * blen) - (rb + 1) * blen;
        }

        // 将元素插入到目标块中
        blocks.get(rb).add(newIndexInBlock, val);
        cnt.get(rb)[val]++;
    }

    /**
     * 初始化分块
     */
    public static void prepare() {
        // 计算块大小，通常选择sqrt(n)
        blen = (int) Math.sqrt(n);
        bcnt = 0;
        blocks.clear();
        cnt.clear();

        // 初始化分块结构
        for (int i = 0; i < n; i++) {
            if (i % blen == 0) {
                blocks.add(new LinkedList<>());
                cnt.add(new int[MAXN]);
                bcnt++;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n
        n = in.nextInt();
        
        // 初始化分块
        prepare();

        // 读取初始数组
        for (int i = 0; i < n; i++) {
            int blockId = i / blen;
            int indexInBlock = i % blen;
            int val = in.nextInt();
            blocks.get(blockId).add(val);
            cnt.get(blockId)[val]++;
        }

        // 读取操作次数q
        q = in.nextInt();
        
        // 处理q次操作
        for (int i = 1, op, l, r; i <= q; i++) {
            op = in.nextInt();
            l = in.nextInt();
            r = in.nextInt();
            if (op == 1) {
                // 查询操作
                out.println(query(l, r));
            } else {
                // 移动操作
                move(l, r);
                // 定期重构以保持效率
                if (i % 5000 == 0) {
                    reBuild();
                }
            }
        }
        
        out.flush();
        out.close();
    }

    // 高效读取工具类，用于加快输入输出速度
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}