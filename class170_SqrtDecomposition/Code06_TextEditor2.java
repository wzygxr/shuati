package class172;

// 文本编辑器，块状链表实现，C++版
// 题目来源：洛谷P4008 [NOI2003] 文本编辑器
// 题目链接：https://www.luogu.com.cn/problem/P4008
// 题目大意：
// 一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作
// Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
// Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
// Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
// Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
// Prev       : 光标前移一个字符，操作保证光标不会到非法位置
// Next       : 光标后移一个字符，操作保证光标不会到非法位置
// Insert操作时，字符串s中ASCII码在[32,126]范围上的字符一定有n个，其他字符请过滤掉
// 测试链接 : https://www.luogu.com.cn/problem/P4008
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 解题思路：
// 使用块状链表实现文本编辑器
// 1. 将文本分成多个块，每个块大小约为2*sqrt(n)
// 2. 使用链表连接各个块
// 3. 对于各种操作，先定位到对应的块和块内位置，然后进行相应处理
// 4. 维护操作：检查相邻块，如果内容大小之和不超过块容量，则合并

// 时间复杂度分析：
// 1. 插入操作：O(sqrt(n) + len)，其中len为插入字符串长度
// 2. 删除操作：O(sqrt(n) + len)，其中len为删除字符串长度
// 3. 查询操作：O(sqrt(n) + len)，其中len为查询字符串长度
// 空间复杂度：O(n)，存储文本内容

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 整个文章能到达的最大长度
//const int MAXN = 3000001;
//// 块内容量，近似等于 2 * 根号n，每块内容大小不会超过容量
//const int BLEN = 3001;
//// 块的数量上限
//const int BNUM = (MAXN / BLEN) << 1;
//
//// 写入内容的空间，编号为i的块，内容写入到space[i]
//char space[BNUM][BLEN];
//// 编号分配池，其实是一个栈，分配编号从栈顶弹出，回收编号从栈顶压入
//int pool[BNUM];
//// 分配池的栈顶
//int top = 0;
//
//// nxt[i]表示编号为i的块，下一块的编号
//int nxt[BNUM];
//// siz[i]表示编号为i的块，写入了多少长度的内容
//int siz[BNUM];
//
//// 插入字符串时，先读入进str，然后写入到块
//// 获取字符串时，先从块里取出内容保留在str，然后打印str
//char str[MAXN];
//
///**
// * 准备工作，初始化分配池和头块配置
// * 时间复杂度：O(BNUM)
// */
//void prepare() {
//    // 初始化分配池，编号从BNUM-1到1
//    for (int i = 1, id = BNUM - 1; i < BNUM; i++, id--) {
//        pool[i] = id;
//    }
//    top = BNUM - 1;
//    // 初始化头块
//    siz[0] = 0;
//    nxt[0] = -1;
//}
//
///**
// * 分配一个块编号
// * 时间复杂度：O(1)
// * @return 块编号
// */
//int assign() {
//    return pool[top--];
//}
//
///**
// * 回收一个块编号
// * 时间复杂度：O(1)
// * @param id 块编号
// */
//void recycle(int id) {
//    pool[++top] = id;
//}
//
//// 寻找整个文章中的pos位置
//// 找到所在块的编号 和 块内位置
//// 块编号设置给bi，块内位置设置给pi
//int bi, pi;
//
///**
// * 查找文章中第pos个字符所在的块和块内位置
// * 时间复杂度：O(sqrt(n))
// * @param pos 字符位置（从1开始计数）
// */
//void find(int pos) {
//    int curb = 0;  // 当前块编号，从头块开始
//    // 遍历块链表，找到包含第pos个字符的块
//    while (curb != -1 && pos > siz[curb]) {
//        pos -= siz[curb];  // 减去当前块的字符数
//        curb = nxt[curb];  // 移动到下一块
//    }
//    bi = curb;  // 所在块编号
//    pi = pos;   // 块内位置
//}
//
///**
// * 连接两个块，并将指定内容写入新块
// * 时间复杂度：O(len)
// * @param curb 当前块编号
// * @param nextb 新块编号
// * @param src 源字符串数组
// * @param pos 源字符串起始位置
// * @param len 写入长度
// */
//void linkAndWrite(int curb, int nextb, char* src, int pos, int len) {
//    nxt[nextb] = nxt[curb];  // 新块的下一块指向当前块的下一块
//    nxt[curb] = nextb;       // 当前块的下一块指向新块
//    // 将源字符串内容复制到新块中
//    memcpy(space[nextb], src + pos, len);
//    siz[nextb] = len;        // 设置新块的大小
//}
//
///**
// * 合并两个相邻的块
// * 时间复杂度：O(siz[nextb])
// * @param curb 当前块编号
// * @param nextb 下一块编号
// */
//void merge(int curb, int nextb) {
//    // 将nextb块的内容复制到curb块的末尾
//    memcpy(space[curb] + siz[curb], space[nextb], siz[nextb]);
//    siz[curb] += siz[nextb];  // 更新curb块的大小
//    nxt[curb] = nxt[nextb];   // 跳过nextb块
//    recycle(nextb);           // 回收nextb块的编号
//}
//
///**
// * 分裂一个块，在指定位置处分裂
// * 时间复杂度：O(siz[bi] - pos)
// * @param curb 块编号
// * @param pos 分裂位置
// */
//void split(int curb, int pos) {
//    // 如果块不存在或分裂位置在块末尾，则无需分裂
//    if (curb == -1 || pos == siz[curb]) {
//        return;
//    }
//    int nextb = assign();  // 分配新块编号
//    // 将curb块pos位置之后的内容写入新块
//    linkAndWrite(curb, nextb, space[curb], pos, siz[curb] - pos);
//    siz[curb] = pos;       // 更新curb块的大小
//}
//
///**
// * 维护操作，合并相邻的小块
// * 时间复杂度：O(块数)
// */
//void maintain() {
//    // 遍历所有块
//    for (int curb = 0, nextb; curb != -1; curb = nxt[curb]) {
//        nextb = nxt[curb];
//        // 合并相邻的小块
//        while (nextb != -1 && siz[curb] + siz[nextb] <= BLEN) {
//            merge(curb, nextb);
//            nextb = nxt[curb];
//        }
//    }
//}
//
///**
// * 在指定位置插入字符串
// * 时间复杂度：O(sqrt(n) + len)
// * @param pos 插入位置
// * @param len 插入字符串长度
// */
//void insert(int pos, int len) {
//    find(pos);           // 找到插入位置所在的块和块内位置
//    split(bi, pi);       // 在插入位置分裂块
//    int curb = bi, newb, done = 0;
//    // 按块大小批量插入
//    while (done + BLEN <= len) {
//        newb = assign();  // 分配新块
//        linkAndWrite(curb, newb, str, done, BLEN);  // 写入一个完整块
//        done += BLEN;
//        curb = newb;
//    }
//    // 插入剩余内容
//    if (len > done) {
//        newb = assign();
//        linkAndWrite(curb, newb, str, done, len - done);
//    }
//    maintain();  // 维护操作
//}
//
///**
// * 从指定位置删除指定长度的字符
// * 时间复杂度：O(sqrt(n) + len)
// * @param pos 删除起始位置
// * @param len 删除长度
// */
//void erase(int pos, int len) {
//    find(pos);           // 找到删除起始位置所在的块和块内位置
//    split(bi, pi);       // 在删除起始位置分裂块
//    int curb = bi, nextb = nxt[curb];
//    // 删除完整的块
//    while (nextb != -1 && len > siz[nextb]) {
//        len -= siz[nextb];
//        recycle(nextb);   // 回收块编号
//        nextb = nxt[nextb];
//    }
//    // 处理最后一个不完整的块
//    if (nextb != -1) {
//        split(nextb, len);  // 分裂最后一个块
//        recycle(nextb);     // 回收块编号
//        nxt[curb] = nxt[nextb];  // 跳过被删除的块
//    } else {
//        nxt[curb] = -1;     // 如果没有下一块，则当前块成为最后一个块
//    }
//    maintain();  // 维护操作
//}
//
///**
// * 获取指定位置开始的指定长度的字符
// * 时间复杂度：O(sqrt(n) + len)
// * @param pos 获取起始位置
// * @param len 获取长度
// */
//void get(int pos, int len) {
//    find(pos);           // 找到获取起始位置所在的块和块内位置
//    int curb = bi;
//    pos = pi;
//    // 获取第一个块的内容
//    int done = (len < siz[curb] - pos) ? len : (siz[curb] - pos);
//    memcpy(str, space[curb] + pos, done);
//    curb = nxt[curb];
//    // 获取后续完整块的内容
//    while (curb != -1 && done + siz[curb] <= len) {
//        memcpy(str + done, space[curb], siz[curb]);
//        done += siz[curb];
//        curb = nxt[curb];
//    }
//    // 获取最后一个不完整块的内容
//    if (curb != -1 && done < len) {
//        memcpy(str + done, space[curb], len - done);
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    int n;
//    cin >> n;            // 操作数
//    int pos = 0;         // 光标位置
//    int len;
//    char op[10];
//    prepare();           // 初始化
//    for (int i = 1; i <= n; i++) {
//        cin >> op;
//        if (op[0] == 'P') {
//            pos--;       // 光标前移
//        } else if (op[0] == 'N') {
//            pos++;       // 光标后移
//        } else if (op[0] == 'M') {
//            cin >> pos;  // 移动光标
//        } else if (op[0] == 'I') {
//            cin >> len;
//            // 读取插入的字符串，过滤非ASCII码字符
//            for (int j = 0; j < len; ) {
//                char ch = cin.get();
//                if (32 <= ch && ch <= 126) {
//                    str[j++] = ch;
//                }
//            }
//            insert(pos, len);    // 插入字符串
//        } else if (op[0] == 'D') {
//            cin >> len;
//            erase(pos, len);     // 删除字符串
//        } else {
//            cin >> len;
//            get(pos, len);       // 获取字符串
//            cout.write(str, len);
//            cout.put('\n');
//        }
//    }
//    return 0;
//}