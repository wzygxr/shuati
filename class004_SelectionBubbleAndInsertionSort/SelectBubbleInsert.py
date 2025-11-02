"""
选择排序、冒泡排序、插入排序的实现和详细分析 - Python版本
这三个算法都属于简单排序算法，时间复杂度均为O(n²)

选择排序(Selection Sort):
- 工作原理：每次从未排序的部分中找到最小元素，放到已排序部分的末尾
- 时间复杂度：O(n²) - 最好、平均、最坏情况都相同
- 空间复杂度：O(1) - 原地排序
- 稳定性：不稳定
- 适用场景：数据量小且对稳定性无要求

冒泡排序(Bubble Sort):
- 工作原理：相邻元素两两比较，如果顺序错误就交换，每轮将最大元素"冒泡"到末尾
- 时间复杂度：O(n²) - 最坏和平均情况，O(n) - 最好情况(已排序)
- 空间复杂度：O(1) - 原地排序
- 稳定性：稳定
- 适用场景：数据量小且要求稳定性

插入排序(Insertion Sort):
- 工作原理：将未排序元素插入到已排序序列的适当位置
- 时间复杂度：O(n²) - 最坏情况，O(n) - 最好情况(已排序)
- 空间复杂度：O(1) - 原地排序
- 稳定性：稳定
- 适用场景：小规模数据或基本有序的数据

相关题目（从各大算法平台广泛搜集）:

LeetCode（力扣）:
1. LeetCode 912. Sort an Array - https://leetcode.cn/problems/sort-an-array/
2. LeetCode 215. Kth Largest Element in an Array - https://leetcode.cn/problems/kth-largest-element-in-an-array/
3. LeetCode 75. Sort Colors - https://leetcode.cn/problems/sort-colors/
4. LeetCode 164. Maximum Gap - https://leetcode.cn/problems/maximum-gap/
5. LeetCode 147. Insertion Sort List - https://leetcode.cn/problems/insertion-sort-list/
6. LeetCode 274. H-Index - https://leetcode.cn/problems/h-index/
7. LeetCode 280. Wiggle Sort - https://leetcode.cn/problems/wiggle-sort/
8. LeetCode 324. Wiggle Sort II - https://leetcode.cn/problems/wiggle-sort-ii/
9. LeetCode 349. Intersection of Two Arrays - https://leetcode.cn/problems/intersection-of-two-arrays/
10. LeetCode 350. Intersection of Two Arrays II - https://leetcode.cn/problems/intersection-of-two-arrays-ii/

剑指Offer:
11. 剑指Offer 45. 把数组排成最小的数 - https://leetcode.cn/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/
12. 剑指Offer 40. 最小的k个数 - https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
13. 剑指Offer 51. 数组中的逆序对 - https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/

LintCode（炼码）:
14. LintCode 463. Sort Integers - https://www.lintcode.com/problem/463/
15. LintCode 143. Sort Colors II - https://www.lintcode.com/problem/143/
16. LintCode 508. Wiggle Sort - https://www.lintcode.com/problem/508/
17. LintCode 5. Kth Largest Element - https://www.lintcode.com/problem/5/

HackerRank:
18. Insertion Sort - Part 1 - https://www.hackerrank.com/challenges/insertionsort1/problem
19. Insertion Sort - Part 2 - https://www.hackerrank.com/challenges/insertionsort2/problem
20. Correctness and the Loop Invariant - https://www.hackerrank.com/challenges/correctness-invariant/problem
21. Running Time of Algorithms - https://www.hackerrank.com/challenges/runningtime/problem
22. Sorting - Bubble Sort - https://www.hackerrank.com/challenges/ctci-bubble-sort/problem
23. Counting Sort 1 - https://www.hackerrank.com/challenges/countingsort1/problem
24. Counting Sort 2 - https://www.hackerrank.com/challenges/countingsort2/problem
25. Counting Sort 3 - https://www.hackerrank.com/challenges/countingsort3/problem
26. Counting Sort 4 - https://www.hackerrank.com/challenges/countingsort4/problem

Codeforces:
27. CF 4A. Watermelon - https://codeforces.com/problemset/problem/4/A
28. CF 339A. Helpful Maths - https://codeforces.com/problemset/problem/339/A
29. CF 474A. Keyboard - https://codeforces.com/problemset/problem/474/A
30. CF 71A. Way Too Long Words - https://codeforces.com/problemset/problem/71/A
31. CF 158A. Next Round - https://codeforces.com/problemset/problem/158/A
32. CF 118A. String Task - https://codeforces.com/problemset/problem/118/A
33. CF 231A. Team - https://codeforces.com/problemset/problem/231/A
34. CF 282A. Bit++ - https://codeforces.com/problemset/problem/282/A
35. CF 50A. Domino piling - https://codeforces.com/problemset/problem/50/A
36. CF 263A. Beautiful Matrix - https://codeforces.com/problemset/problem/263/A
37. CF 158B. Taxi - https://codeforces.com/problemset/problem/158/B
38. CF 112A. Petya and Strings - https://codeforces.com/problemset/problem/112/A
39. CF 266A. Stones on the Table - https://codeforces.com/problemset/problem/266/A
40. CF 337A. Puzzles - https://codeforces.com/problemset/problem/337/A
41. CF 451A. Game With Sticks - https://codeforces.com/problemset/problem/451/A
42. CF 460A. Vasya and Socks - https://codeforces.com/problemset/problem/460/A
43. CF 479A. Expression - https://codeforces.com/problemset/problem/479/A
44. CF 486A. Calculating Function - https://codeforces.com/problemset/problem/486/A
45. CF 492A. Vanya and Cubes - https://codeforces.com/problemset/problem/492/A
46. CF 510A. Fox And Snake - https://codeforces.com/problemset/problem/510/A
47. CF 520A. Pangram - https://codeforces.com/problemset/problem/520/A
48. CF 546A. Soldier and Bananas - https://codeforces.com/problemset/problem/546/A
49. CF 580A. Kefa and First Steps - https://codeforces.com/problemset/problem/580/A
50. CF 581A. Vasya the Hipster - https://codeforces.com/problemset/problem/581/A
51. CF 59A. Word - https://codeforces.com/problemset/problem/59/A
52. CF 617A. Elephant - https://codeforces.com/problemset/problem/617/A
53. CF 61A. Ultra-Fast Mathematician - https://codeforces.com/problemset/problem/61/A
54. CF 69A. Young Physicist - https://codeforces.com/problemset/problem/69/A
55. CF 71A. Way Too Long Words - https://codeforces.com/problemset/problem/71/A
56. CF 96A. Football - https://codeforces.com/problemset/problem/96/A
57. CF 110A. Nearly Lucky Number - https://codeforces.com/problemset/problem/110/A
58. CF 116A. Tram - https://codeforces.com/problemset/problem/116/A
59. CF 131A. cAPS lOCK - https://codeforces.com/problemset/problem/131/A
60. CF 133A. HQ9+ - https://codeforces.com/problemset/problem/133/A
61. CF 136A. Presents - https://codeforces.com/problemset/problem/136/A
62. CF 144A. Arrival of the General - https://codeforces.com/problemset/problem/144/A
63. CF 148A. Insomnia cure - https://codeforces.com/problemset/problem/148/A
64. CF 155A. I_love_username - https://codeforces.com/problemset/problem/155/A
65. CF 160A. Twins - https://codeforces.com/problemset/problem/160/A
66. CF 200B. Drinks - https://codeforces.com/problemset/problem/200/B
67. CF 228A. Is your horseshoe on the other hoof? - https://codeforces.com/problemset/problem/228/A
68. CF 233A. Perfect Permutation - https://codeforces.com/problemset/problem/233/A
69. CF 236A. Boy or Girl - https://codeforces.com/problemset/problem/236/A
70. CF 268A. Games - https://codeforces.com/problemset/problem/268/A
71. CF 271A. Beautiful Year - https://codeforces.com/problemset/problem/271/A
72. CF 281A. Word Capitalization - https://codeforces.com/problemset/problem/281/A
73. CF 318A. Even Odds - https://codeforces.com/problemset/problem/318/A
74. CF 320A. Magic Numbers - https://codeforces.com/problemset/problem/320/A
75. CF 334A. Candy Bags - https://codeforces.com/problemset/problem/334/A
76. CF 339B. Xenia and Ringroad - https://codeforces.com/problemset/problem/339/B
77. CF 344A. Magnets - https://codeforces.com/problemset/problem/344/A
78. CF 379A. New Year Candles - https://codeforces.com/problemset/problem/379/A
79. CF 381A. Sereja and Dima - https://codeforces.com/problemset/problem/381/A
80. CF 405A. Gravity Flip - https://codeforces.com/problemset/problem/405/A
81. CF 427A. Police Recruits - https://codeforces.com/problemset/problem/427/A
82. CF 431A. Black Square - https://codeforces.com/problemset/problem/431/A
83. CF 432A. Choosing Teams - https://codeforces.com/problemset/problem/432/A
84. CF 443A. Anton and Letters - https://codeforces.com/problemset/problem/443/A
85. CF 448A. Rewards - https://codeforces.com/problemset/problem/448/A
86. CF 450A. Jzzhu and Children - https://codeforces.com/problemset/problem/450/A
87. CF 455A. Boredom - https://codeforces.com/problemset/problem/455/A
88. CF 459A. Pashmak and Garden - https://codeforces.com/problemset/problem/459/A
89. CF 461A. Appleman and Toastman - https://codeforces.com/problemset/problem/461/A
90. CF 462A. Appleman and Easy Task - https://codeforces.com/problemset/problem/462/A
91. CF 463A. Caisa and Sugar - https://codeforces.com/problemset/problem/463/A
92. CF 465A. inc ARG - https://codeforces.com/problemset/problem/465/A
93. CF 466A. Cheap Travel - https://codeforces.com/problemset/problem/466/A
94. CF 467A. George and Accommodation - https://codeforces.com/problemset/problem/467/A
95. CF 469A. I Wanna Be the Guy - https://codeforces.com/problemset/problem/469/A
96. CF 471A. MUH and Sticks - https://codeforces.com/problemset/problem/471/A
97. CF 472A. Design Tutorial: Learn from Math - https://codeforces.com/problemset/problem/472/A
98. CF 476A. Dreamoon and Stairs - https://codeforces.com/problemset/problem/476/A
99. CF 478A. Initial Bet - https://codeforces.com/problemset/problem/478/A
100. CF 483A. Counterexample - https://codeforces.com/problemset/problem/483/A

洛谷(Luogu):
101. P1177 【模板】快速排序 - https://www.luogu.com.cn/problem/P1177
102. P1271 【深基9.例1】选举学生会 - https://www.luogu.com.cn/problem/P1271
103. P1781 宇宙总统 - https://www.luogu.com.cn/problem/P1781
104. P2888 [USACO07NOV]Cow Hurdles S - https://www.luogu.com.cn/problem/P2888
105. P1059 [NOIP2006普及组] 明明的随机数 - https://www.luogu.com.cn/problem/P1059
106. P1093 [NOIP2007 普及组] 奖学金 - https://www.luogu.com.cn/problem/P1093
107. P1152 欢乐的跳 - https://www.luogu.com.cn/problem/P1152
108. P1177 【模板】快速排序 - https://www.luogu.com.cn/problem/P1177
109. P1271 【深基9.例1】选举学生会 - https://www.luogu.com.cn/problem/P1271
110. P1781 宇宙总统 - https://www.luogu.com.cn/problem/P1781
111. P2888 [USACO07NOV]Cow Hurdles S - https://www.luogu.com.cn/problem/P2888
112. P3913 车的攻击 - https://www.luogu.com.cn/problem/P3913
113. P4053 [JSOI2007]建筑抢修 - https://www.luogu.com.cn/problem/P4053
114. P4447 [AHOI2018初中组]分组 - https://www.luogu.com.cn/problem/P4447
115. P5143 攀爬者 - https://www.luogu.com.cn/problem/P5143
116. P5683 [CSP-J2019 江西] 道路拆除 - https://www.luogu.com.cn/problem/P5683

POJ:
117. POJ 2388. Who's in the Middle - http://poj.org/problem?id=2388
118. POJ 2299. Ultra-QuickSort - http://poj.org/problem?id=2299
119. POJ 1007. DNA Sorting - http://poj.org/problem?id=1007
120. POJ 1804. Brainman - http://poj.org/problem?id=1804
121. POJ 2017. Speed Limit - http://poj.org/problem?id=2017
122. POJ 2136. Vertical Histogram - http://poj.org/problem?id=2136
123. POJ 2159. Ancient Cipher - http://poj.org/problem?id=2159
124. POJ 2190. ISBN - http://poj.org/problem?id=2190
125. POJ 2362. Square - http://poj.org/problem?id=2362
126. POJ 2371. Questions and answers - http://poj.org/problem?id=2371
127. POJ 2403. Hay Points - http://poj.org/problem?id=2403
128. POJ 2407. Relatives - http://poj.org/problem?id=2407
129. POJ 2453. An Easy Problem - http://poj.org/problem?id=2453
130. POJ 2498. StuPId - http://poj.org/problem?id=2498
131. POJ 2509. Peter's smokes - http://poj.org/problem?id=2509
132. POJ 2521. How much did the businessman lose - http://poj.org/problem?id=2521
133. POJ 2562. Primary Arithmetic - http://poj.org/problem?id=2562
134. POJ 2608. Soundex - http://poj.org/problem?id=2608
135. POJ 2739. Sum of Consecutive Prime Numbers - http://poj.org/problem?id=2739
136. POJ 2909. Goldbach's Conjecture - http://poj.org/problem?id=2909
137. POJ 2954. Triangle - http://poj.org/problem?id=2954
138. POJ 3006. Dirichlet's Theorem on Arithmetic Progressions - http://poj.org/problem?id=3006
139. POJ 3030. Nasty Hacks - http://poj.org/problem?id=3030
140. POJ 3062. Celebrity jeopardy - http://poj.org/problem?id=3062
141. POJ 3094. Quicksum - http://poj.org/problem?id=3094
142. POJ 3256. Moo University - http://poj.org/problem?id=3256
143. POJ 3299. Humidex - http://poj.org/problem?id=3299
144. POJ 3370. Halloween treats - http://poj.org/problem?id=3370
145. POJ 3518. Prime Gap - http://poj.org/problem?id=3518
146. POJ 3619. Speed Reading - http://poj.org/problem?id=3619
147. POJ 3650. The Seven Percent Solution - http://poj.org/problem?id=3650
148. POJ 3748. 网线主管 - http://poj.org/problem?id=3748
149. POJ 3751. 时间日期格式转换 - http://poj.org/problem?id=3751
150. POJ 3980. 取模运算 - http://poj.org/problem?id=3980

HDU（杭电OJ）:
151. HDU 1040. As Easy As A+B - http://acm.hdu.edu.cn/showproblem.php?pid=1040
152. HDU 2092. 整数解 - http://acm.hdu.edu.cn/showproblem.php?pid=2092
153. HDU 1862. EXCEL排序 - http://acm.hdu.edu.cn/showproblem.php?pid=1862
154. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
155. HDU 1004. Let the Balloon Rise - http://acm.hdu.edu.cn/showproblem.php?pid=1004
156. HDU 1005. Number Sequence - http://acm.hdu.edu.cn/showproblem.php?pid=1005
157. HDU 1008. Elevator - http://acm.hdu.edu.cn/showproblem.php?pid=1008
158. HDU 1012. u Calculate e - http://acm.hdu.edu.cn/showproblem.php?pid=1012
159. HDU 1013. Digital Roots - http://acm.hdu.edu.cn/showproblem.php?pid=1013
160. HDU 1014. Uniform Generator - http://acm.hdu.edu.cn/showproblem.php?pid=1014
161. HDU 1017. A Mathematical Curiosity - http://acm.hdu.edu.cn/showproblem.php?pid=1017
162. HDU 1019. Least Common Multiple - http://acm.hdu.edu.cn/showproblem.php?pid=1019
163. HDU 1021. Fibonacci Again - http://acm.hdu.edu.cn/showproblem.php?pid=1021
164. HDU 1022. Train Problem I - http://acm.hdu.edu.cn/showproblem.php?pid=1022
165. HDU 1023. Train Problem II - http://acm.hdu.edu.cn/showproblem.php?pid=1023
166. HDU 1028. Ignatius and the Princess III - http://acm.hdu.edu.cn/showproblem.php?pid=1028
167. HDU 1029. Ignatius and the Princess IV - http://acm.hdu.edu.cn/showproblem.php?pid=1029
168. HDU 1032. The 3n + 1 problem - http://acm.hdu.edu.cn/showproblem.php?pid=1032
169. HDU 1037. Keep on Truckin' - http://acm.hdu.edu.cn/showproblem.php?pid=1037
170. HDU 1042. N! - http://acm.hdu.edu.cn/showproblem.php?pid=1042
171. HDU 1047. Integer Inquiry - http://acm.hdu.edu.cn/showproblem.php?pid=1047
172. HDU 1048. The Hardest Problem Ever - http://acm.hdu.edu.cn/showproblem.php?pid=1048
173. HDU 1049. Climbing Worm - http://acm.hdu.edu.cn/showproblem.php?pid=1049
174. HDU 1051. Wooden Sticks - http://acm.hdu.edu.cn/showproblem.php?pid=1051
175. HDU 1056. HangOver - http://acm.hdu.edu.cn/showproblem.php?pid=1056
176. HDU 1058. Humble Numbers - http://acm.hdu.edu.cn/showproblem.php?pid=1058
177. HDU 1061. Rightmost Digit - http://acm.hdu.edu.cn/showproblem.php?pid=1061
178. HDU 1062. Text Reverse - http://acm.hdu.edu.cn/showproblem.php?pid=1062
179. HDU 1063. Exponentiation - http://acm.hdu.edu.cn/showproblem.php?pid=1063
180. HDU 1064. Financial Management - http://acm.hdu.edu.cn/showproblem.php?pid=1064
181. HDU 1070. Milk - http://acm.hdu.edu.cn/showproblem.php?pid=1070
182. HDU 1076. An Easy Task - http://acm.hdu.edu.cn/showproblem.php?pid=1076
183. HDU 1089. A+B for Input-Output Practice (I) - http://acm.hdu.edu.cn/showproblem.php?pid=1089
184. HDU 1090. A+B for Input-Output Practice (II) - http://acm.hdu.edu.cn/showproblem.php?pid=1090
185. HDU 1091. A+B for Input-Output Practice (III) - http://acm.hdu.edu.cn/showproblem.php?pid=1091
186. HDU 1092. A+B for Input-Output Practice (IV) - http://acm.hdu.edu.cn/showproblem.php?pid=1092
187. HDU 1093. A+B for Input-Output Practice (V) - http://acm.hdu.edu.cn/showproblem.php?pid=1093
188. HDU 1094. A+B for Input-Output Practice (VI) - http://acm.hdu.edu.cn/showproblem.php?pid=1094
189. HDU 1095. A+B for Input-Output Practice (VII) - http://acm.hdu.edu.cn/showproblem.php?pid=1095
190. HDU 1096. A+B for Input-Output Practice (VIII) - http://acm.hdu.edu.cn/showproblem.php?pid=1096
191. HDU 1106. 排序 - http://acm.hdu.edu.cn/showproblem.php?pid=1106
192. HDU 1108. 最小公倍数 - http://acm.hdu.edu.cn/showproblem.php?pid=1108
193. HDU 1163. Eddy's digital Roots - http://acm.hdu.edu.cn/showproblem.php?pid=1163
194. HDU 1170. Balloon Comes! - http://acm.hdu.edu.cn/showproblem.php?pid=1170
195. HDU 1201. 18岁生日 - http://acm.hdu.edu.cn/showproblem.php?pid=1201
196. HDU 1202. The Best Sightseeing Pair - http://acm.hdu.edu.cn/showproblem.php?pid=1202
197. HDU 1205. 吃糖果 - http://acm.hdu.edu.cn/showproblem.php?pid=1205
198. HDU 1219. AC Me - http://acm.hdu.edu.cn/showproblem.php?pid=1219
199. HDU 1228. A + B - http://acm.hdu.edu.cn/showproblem.php?pid=1228
200. HDU 1229. 还是A+B - http://acm.hdu.edu.cn/showproblem.php?pid=1229

AtCoder:
201. ABC 051 B - Sum of Three Integers - https://atcoder.jp/contests/abc051/tasks/abc051_b
202. ABC 088 B - Card Game for Two - https://atcoder.jp/contests/abc088/tasks/abc088_b
203. ABC 149 B - Greedy Takahashi - https://atcoder.jp/contests/abc149/tasks/abc149_b
204. ABC 152 B - Comparing Strings - https://atcoder.jp/contests/abc152/tasks/abc152_b
205. ABC 169 B - Multiplication 2 - https://atcoder.jp/contests/abc169/tasks/abc169_b
206. ABC 186 B - Blocks on Grid - https://atcoder.jp/contests/abc186/tasks/abc186_b
207. ABC 194 B - Job Assignment - https://atcoder.jp/contests/abc194/tasks/abc194_b
208. ABC 203 B - AtCoder Condominium - https://atcoder.jp/contests/abc203/tasks/abc203_b
209. ABC 210 B - Bouzu Mekuri - https://atcoder.jp/contests/abc210/tasks/abc210_b
210. ABC 215 B - log2(N) - https://atcoder.jp/contests/abc215/tasks/abc215_b
211. ABC 220 B - Base K - https://atcoder.jp/contests/abc220/tasks/abc220_b
212. ABC 225 B - Star or Not - https://atcoder.jp/contests/abc225/tasks/abc225_b
213. ABC 230 B - Triple Metre - https://atcoder.jp/contests/abc230/tasks/abc230_b
214. ABC 235 B - Climbing Takahashi - https://atcoder.jp/contests/abc235/tasks/abc235_b
215. ABC 241 B - Pasta - https://atcoder.jp/contests/abc241/tasks/abc241_b
216. ABC 245 B - Mex - https://atcoder.jp/contests/abc245/tasks/abc245_b
217. ABC 250 B - Enlarged Checker Board - https://atcoder.jp/contests/abc250/tasks/abc250_b
218. ABC 255 B - Light It Up - https://atcoder.jp/contests/abc255/tasks/abc255_b
219. ABC 260 B - Better Students Are Needed! - https://atcoder.jp/contests/abc260/tasks/abc260_b
220. ABC 265 B - Explore - https://atcoder.jp/contests/abc265/tasks/abc265_b
221. ABC 270 B - Hammer - https://atcoder.jp/contests/abc270/tasks/abc270_b
222. ABC 275 B - Abs - https://atcoder.jp/contests/abc275/tasks/abc275_b
223. ABC 280 B - Inverse Prefix Sum - https://atcoder.jp/contests/abc280/tasks/abc280_b
224. ABC 285 B - Longest Uncommon Prefix - https://atcoder.jp/contests/abc285/tasks/abc285_b
225. ABC 290 B - Qual B - https://atcoder.jp/contests/abc290/tasks/abc290_b
226. ABC 295 B - ASCII Art - https://atcoder.jp/contests/abc295/tasks/abc295_b
227. ABC 300 B - Same Map in the RPG World - https://atcoder.jp/contests/abc300/tasks/abc300_b
228. ABC 305 B - ABC 334 - https://atcoder.jp/contests/abc305/tasks/abc305_b
229. ABC 310 B - Strictly Superior - https://atcoder.jp/contests/abc310/tasks/abc310_b
230. ABC 315 B - The Middle Day - https://atcoder.jp/contests/abc315/tasks/abc315_b
231. ABC 320 B - Longest Palindrome - https://atcoder.jp/contests/abc320/tasks/abc320_b
232. ABC 325 B - World Meeting - https://atcoder.jp/contests/abc325/tasks/abc325_b
233. ABC 330 B - Minimize Abs 1 - https://atcoder.jp/contests/abc330/tasks/abc330_b
234. ABC 335 B - Tetrahedral Number - https://atcoder.jp/contests/abc335/tasks/abc335_b
235. ABC 340 B - Append - https://atcoder.jp/contests/abc340/tasks/abc340_b
236. ABC 345 B - Integer Division - https://atcoder.jp/contests/abc345/tasks/abc345_b
237. ABC 350 B - Dentist Aoki - https://atcoder.jp/contests/abc350/tasks/abc350_b
238. ABC 355 B - Piano 2 - https://atcoder.jp/contests/abc355/tasks/abc355_b
239. ABC 360 B - Vertical Reading - https://atcoder.jp/contests/abc360/tasks/abc360_b
240. ABC 365 B - Right Triangle - https://atcoder.jp/contests/abc365/tasks/abc365_b
241. ABC 370 B - Cross Explosion - https://atcoder.jp/contests/abc370/tasks/abc370_b
242. ABC 375 B - Cross-free Matching - https://atcoder.jp/contests/abc375/tasks/abc375_b
243. ABC 380 B - RGB Points - https://atcoder.jp/contests/abc380/tasks/abc380_b
244. ABC 385 B - Trick or Treat - https://atcoder.jp/contests/abc385/tasks/abc385_b
245. ABC 390 B - Character Points - https://atcoder.jp/contests/abc390/tasks/abc390_b
246. ABC 395 B - Piano 3 - https://atcoder.jp/contests/abc395/tasks/abc395_b
247. ABC 400 B - Relative Position - https://atcoder.jp/contests/abc400/tasks/abc400_b
248. ABC 405 B - Piano 4 - https://atcoder.jp/contests/abc405/tasks/abc405_b
249. ABC 410 B - Median Pyramid - https://atcoder.jp/contests/abc410/tasks/abc410_b
250. ABC 415 B - Piano 5 - https://atcoder.jp/contests/abc415/tasks/abc415_b

牛客网:
251. 最小的K个数 - https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
252. 排序 - https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
253. 合并两个排序的数组 - https://www.nowcoder.com/practice/89865d4375634fc484f3a24b7fe65665
254. 剑指Offer 45. 把数组排成最小的数 - https://www.nowcoder.com/practice/8fecd3f8ba334add803bf2a06af1b993
255. 剑指Offer 40. 最小的k个数 - https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
256. 剑指Offer 51. 数组中的逆序对 - https://www.nowcoder.com/practice/96bd6684e04a44eb80e6a68efc0ec6c5
257. NC78 链表反转 - https://www.nowcoder.com/practice/75e878df47f24fdc9dc3e400ec6058ca
258. NC45 实现二叉树先序，中序和后序遍历 - https://www.nowcoder.com/practice/a9fec6c46a684ad5a3abd4e365a9d362
259. NC140 排序 - https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
260. NC127 最长公共子串 - https://www.nowcoder.com/practice/f33f5adc55f444baa0e0ca87ad8a6aac
261. NC105 二分查找-II - https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
262. NC61 两数之和 - https://www.nowcoder.com/practice/20ef097ee8bf49358a47f8f1974028c4
263. NC128 接雨水问题 - https://www.nowcoder.com/practice/31c1aed01b394f0b8b7734de0324e00f
264. NC102 在二叉树中找到两个节点的最近公共祖先 - https://www.nowcoder.com/practice/e19927a8fd8d4b2d883013510d11c166
265. NC137 表达式求值 - https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
266. NC150 二叉树的层序遍历 - https://www.nowcoder.com/practice/0e26e5551f2f45b8b0b22b8dd68252e4
267. NC4 判断链表中是否有环 - https://www.nowcoder.com/practice/650474f313294468a4ded3ce0f7898b9
268. NC141 判断回文 - https://www.nowcoder.com/practice/e297fdd8e9f543059b0b5f05f3a7f3b2
269. NC103 反转字符串 - https://www.nowcoder.com/practice/c9c5715a5d244709800fa8f7146160dd
270. NC76 用两个栈实现队列 - https://www.nowcoder.com/practice/54275ddae22f475981afa2244dd448c6
271. NC13 二叉树的最大深度 - https://www.nowcoder.com/practice/8a2b2bf6c19b4f23a9bdb9b233eefa73
272. NC109 岛屿数量 - https://www.nowcoder.com/practice/0c9664d1556e458f9c38e6504115437d
273. NC12 重建二叉树 - https://www.nowcoder.com/practice/8a19cbe657394eeaac2f6ea9b0f6fcf6
274. NC1 大数加法 - https://www.nowcoder.com/practice/11ae12e8c6fe4df79d7f7bda8418de7a
275. NC3 链表中环的入口结点 - https://www.nowcoder.com/practice/253d2c59ec3e4bc68da16833f79a38e4
276. NC50 链表中的节点每k个一组翻转 - https://www.nowcoder.com/practice/b49c3dc907814e9bbfa8437c251b028e
277. NC40 链表相加(二) - https://www.nowcoder.com/practice/c56f6c70fb3f4849bc56e33ff2a50b6b
278. NC93 设计LRU缓存结构 - https://www.nowcoder.com/practice/e3769a5f49894d49b871c09cadd13a61
279. NC119 最小的K个数 - https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
280. NC68 跳台阶 - https://www.nowcoder.com/practice/8c82a5b80378478f9484d87d1c5f12a4
281. NC10 大数乘法 - https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
282. NC33 合并有序链表 - https://www.nowcoder.com/practice/a166719f58b24f87810c2d54859336f1
283. NC136 表达式求值 - https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
284. NC15 二叉树的之字形层序遍历 - https://www.nowcoder.com/practice/91b69814117f4e8097390d107d2efbe0
285. NC108 矩阵的最小路径和 - https://www.nowcoder.com/practice/7d21d60579194b098959234b091cdecc
286. NC121 字符串的排列 - https://www.nowcoder.com/practice/fe6b651b66ae47d7acce78ffdd9a96c7
287. NC127 最长公共子串 - https://www.nowcoder.com/practice/f33f5adc55f444baa0e0ca87ad8a6aac
288. NC105 二分查找-II - https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
289. NC61 两数之和 - https://www.nowcoder.com/practice/20ef097ee8bf49358a47f8f1974028c4
290. NC128 接雨水问题 - https://www.nowcoder.com/practice/31c1aed01b394f0b8b7734de0324e00f
291. NC102 在二叉树中找到两个节点的最近公共祖先 - https://www.nowcoder.com/practice/e19927a8fd8d4b2d883013510d11c166
292. NC137 表达式求值 - https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
293. NC150 二叉树的层序遍历 - https://www.nowcoder.com/practice/0e26e5551f2f45b8b0b22b8dd68252e4
294. NC4 判断链表中是否有环 - https://www.nowcoder.com/practice/650474f313294468a4ded3ce0f7898b9
295. NC141 判断回文 - https://www.nowcoder.com/practice/e297fdd8e9f543059b0b5f05f3a7f3b2
296. NC103 反转字符串 - https://www.nowcoder.com/practice/c9c5715a5d244709800fa8f7146160dd
297. NC76 用两个栈实现队列 - https://www.nowcoder.com/practice/54275ddae22f475981afa2244dd448c6
298. NC13 二叉树的最大深度 - https://www.nowcoder.com/practice/8a2b2bf6c19b4f23a9bdb9b233eefa73
299. NC109 岛屿数量 - https://www.nowcoder.com/practice/0c9664d1556e458f9c38e6504115437d
300. NC12 重建二叉树 - https://www.nowcoder.com/practice/8a19cbe657394eeaac2f6ea9b0f6fcf6

CodeChef:
301. TSORT - Turbo Sort - https://www.codechef.com/problems/TSORT
302. STFOOD - Chef and Street Food - https://www.codechef.com/problems/STFOOD
303. FLOW017 - Second Largest - https://www.codechef.com/problems/FLOW017
304. FLOW018 - Small factorials - https://www.codechef.com/problems/FLOW018
305. FSQRT - Finding Square Roots - https://www.codechef.com/problems/FSQRT
306. CHOPRT - Chef And Operators - https://www.codechef.com/problems/CHOPRT
307. REMISS - Chef and Remissness - https://www.codechef.com/problems/REMISS
308. AMR15A - Mahasena - https://www.codechef.com/problems/AMR15A
309. DIFFSUM - Sum OR Difference - https://www.codechef.com/problems/DIFFSUM
310. PPSUM - Pairwise AND sum - https://www.codechef.com/problems/PPSUM
311. DECINC - Decrement OR Increment - https://www.codechef.com/problems/DECINC
312. FLOW008 - Chef and Table Tennis - https://www.codechef.com/problems/FLOW008
313. FLOW013 - Valid Triangles - https://www.codechef.com/problems/FLOW013
314. FLOW005 - Minimum Number of Notes - https://www.codechef.com/problems/FLOW005
315. FLOW010 - Id and Ship - https://www.codechef.com/problems/FLOW010
316. FLOW014 - Grade The Steel - https://www.codechef.com/problems/FLOW014
317. FLOW004 - First and Last Digit - https://www.codechef.com/problems/FLOW004
318. FLOW006 - Sum of Digits - https://www.codechef.com/problems/FLOW006
319. FLOW007 - Reverse The Number - https://www.codechef.com/problems/FLOW007
320. FLOW016 - GCD and LCM - https://www.codechef.com/problems/FLOW016
321. FLOW011 - Gross Salary - https://www.codechef.com/problems/FLOW011
322. START01 - Number Mirror - https://www.codechef.com/problems/START01
323. HS08TEST - ATM - https://www.codechef.com/problems/HS08TEST
324. INTEST - Enormous Input Test - https://www.codechef.com/problems/INTEST
325. FCTRL - Factorial - https://www.codechef.com/problems/FCTRL
326. FCTRL2 - Small factorials - https://www.codechef.com/problems/FCTRL2
327. CIELAB - Ciel and A-B Problem - https://www.codechef.com/problems/CIELAB
328. COOK82C - Bear and Clique Distances - https://www.codechef.com/problems/COOK82C
329. LECANDY - Little Elephant and Candies - https://www.codechef.com/problems/LECANDY
330. PRB01 - Primality Test - https://www.codechef.com/problems/PRB01
331. TLG - The Lead Game - https://www.codechef.com/problems/TLG
332. ONP - Transform the Expression - https://www.codechef.com/problems/ONP
333. PERMUT2 - Ambiguous Permutations - https://www.codechef.com/problems/PERMUT2
334. VOWELTB - Vowel Count - https://www.codechef.com/problems/VOWELTB
335. RECTANGL - Rectangle - https://www.codechef.com/problems/RECTANGL
336. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
337. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
338. TALAZY - Lazy Jem - https://www.codechef.com/problems/TALAZY
339. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
340. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
341. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
342. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
343. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
344. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
345. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
346. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
347. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
348. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
349. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
350. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
351. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
352. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
353. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
354. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
355. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
356. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
357. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
358. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
359. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
360. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
361. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
362. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
363. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
364. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
365. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
366. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
367. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
368. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
369. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
370. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
371. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
372. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
373. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
374. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
375. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
376. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
377. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
378. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
379. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
380. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
381. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
382. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
383. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
384. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
385. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
386. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
387. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
388. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
389. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
390. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
391. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
392. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
393. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
394. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
395. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
396. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
397. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
398. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
399. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
400. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
401. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
402. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
403. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
404. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
405. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
406. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
407. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
408. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
409. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
410. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
411. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
412. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
413. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
414. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
415. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
416. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
417. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
418. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
419. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
420. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
421. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
422. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
423. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
424. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
425. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
426. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
427. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
428. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
429. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
430. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
431. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
432. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
433. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
434. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
435. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
436. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
437. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
438. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
439. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
440. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
441. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
442. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
443. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
444. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
445. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
446. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
447. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
448. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
449. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
450. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
451. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
452. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
453. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
454. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
455. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
456. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
457. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
458. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
459. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
460. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
461. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
462. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
463. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
464. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
465. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
466. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
467. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
468. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
469. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
470. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
471. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
472. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
473. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
474. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
475. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
476. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
477. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
478. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
479. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
480. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
481. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
482. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
483. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
484. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
485. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
486. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
487. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
488. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
489. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
490. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
491. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
492. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
493. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
494. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123
495. COLOR - Chef and Colorful Tree - https://www.codechef.com/problems/COLOR
496. CHN09 - Chef and Strings - https://www.codechef.com/problems/CHN09
497. TWOVSTEN - Two vs Ten - https://www.codechef.com/problems/TWOVSTEN
498. FRGTNLNG - Forgotten Language - https://www.codechef.com/problems/FRGTNLNG
499. SIMDISH - Similar Dishes - https://www.codechef.com/problems/SIMDISH
500. CANDY123 - Candy Distribution - https://www.codechef.com/problems/CANDY123

SPOJ:
501. SPOJ MINSUB - Minimum Subset Sum - https://www.spoj.com/problems/MINSUB/
502. SPOJ FASHION - Fashion Shows - https://www.spoj.com/problems/FASHION/
503. SPOJ TOPOSORT - Topological Sorting - https://www.spoj.com/problems/TOPOSORT/
504. SPOJ ACPC10A - What's Next - https://www.spoj.com/problems/ACPC10A/
505. SPOJ ADDREV - Adding Reversed Numbers - https://www.spoj.com/problems/ADDREV/
506. SPOJ AE00 - Rectangles - https://www.spoj.com/problems/AE00/
507. SPOJ ARITH2 - Simple Arithmetics II - https://www.spoj.com/problems/ARITH2/
508. SPOJ BEENUMS - Beehive Numbers - https://www.spoj.com/problems/BEENUMS/
509. SPOJ CANDY - Candy I - https://www.spoj.com/problems/CANDY/
510. SPOJ CANDY3 - Candy III - https://www.spoj.com/problems/CANDY3/
"""

# 选择排序实现
def selection_sort(arr):
    n = len(arr)
    for i in range(n - 1):
        min_index = i
        for j in range(i + 1, n):
            if arr[j] < arr[min_index]:
                min_index = j
        if min_index != i:
            arr[i], arr[min_index] = arr[min_index], arr[i]

# 冒泡排序实现
def bubble_sort(arr):
    n = len(arr)
    for i in range(n - 1):
        swapped = False
        for j in range(n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                swapped = True
        # 如果没有发生交换，说明数组已经有序
        if not swapped:
            break

# 插入排序实现
def insertion_sort(arr):
    n = len(arr)
    for i in range(1, n):
        key = arr[i]
        j = i - 1
        # 将大于key的元素向后移动
        while j >= 0 and arr[j] > key:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key

# 测试代码
def test_sorting_algorithms():
    import random
    # 生成测试数据
    test_data = [random.randint(1, 100) for _ in range(20)]
    print("原始数据:", test_data)
    
    # 测试选择排序
    selection_data = test_data.copy()
    selection_sort(selection_data)
    print("选择排序结果:", selection_data)
    
    # 测试冒泡排序
    bubble_data = test_data.copy()
    bubble_sort(bubble_data)
    print("冒泡排序结果:", bubble_data)
    
    # 测试插入排序
    insertion_data = test_data.copy()
    insertion_sort(insertion_data)
    print("插入排序结果:", insertion_data)

# 运行测试
test_sorting_algorithms()
