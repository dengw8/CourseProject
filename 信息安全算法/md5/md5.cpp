#include <iostream>
#include <cmath>

using namespace std;

#define F(x, y, z) ((x & y) | (~x & z))
#define G(x, y, z) ((x & z) | (y & ~z))
#define H(x, y, z) (x ^ y ^ z)
#define I(x, y, z) (y ^ (x | (~z)))
#define ROTATE_LEFT(x, n) ((x << n) | (x >> (32 - n)))

#define FF(a, b, c, d, x, s, ac)  \
    \
{                          \
        a += F(b, c, d) + x + ac; \
        a = ROTATE_LEFT(a, s);    \
        a += b;                   \
    \
}
#define GG(a, b, c, d, x, s, ac)  \
    \
{                          \
        a += G(b, c, d) + x + ac; \
        a = ROTATE_LEFT(a, s);    \
        a += b;                   \
    \
}
#define HH(a, b, c, d, x, s, ac)  \
    \
{                          \
        a += H(b, c, d) + x + ac; \
        a = ROTATE_LEFT(a, s);    \
        a += b;                   \
    \
}
#define II(a, b, c, d, x, s, ac)  \
    \
{                          \
        a += I(b, c, d) + x + ac; \
        a = ROTATE_LEFT(a, s);    \
        a += b;                   \
    \
}

/**/
struct CV
{
    unsigned int A; 
    unsigned int B;
    unsigned int C;
    unsigned int D;
};

//将输入的字符串转化为二进制数
void str_to_bint(string cleartext, int *md5)
{
    int len = cleartext.length();
    for (int i = 0; i < len; i++)
    {
        char temp = cleartext[i];
        for (int j = 0; j < 8; j++)
        {
            md5[8 * i + 7 - j] = temp % 2;
            temp /= 2;
        }
    }
}

//填充函数
int func_padding(int *md5, int len, string cleartext)
{
    int len_o = len % 512;
    md5[len] = 1;
    if (len_o < 448)
        len += 512 - len_o;
    else
        len += 512 + 512 - len_o;
    unsigned int length = cleartext.length() * 8;
    int i = 33;
    int cnt = 0;
    while (length)
    {
        if (length % 2)
            md5[len - i - (3 - cnt) * 8] = 1;
        length /= 2;
        i++;
        if (i % 8 == 0)
            cnt++;
        if (cnt > 3)
            cnt = 0;
        if (i > 64)
            i -= 64;
        else if (i == 32)
            break;
    }
    return len;
}

unsigned int ToLittleEndian(int *bin)
{
    unsigned int res = 0;
    for (int i = 7; i >= 0; i--)
        if (bin[i])
            res += pow(2, 7 - i);
    for (int i = 15; i >= 8; i--)
        if (bin[i])
            res += pow(2, 15 - i + 8);
    for (int i = 23; i >= 16; i--)
        if (bin[i])
            res += pow(2, 23 - i + 16);
    for (int i = 31; i >= 24; i--)
        if (bin[i])
            res += pow(2, 31 - i + 24);
    return res;
}

//以字节形式表示整数
unsigned int IntToByte(unsigned int x)
{
    int x1 = x % 256;
    int x2 = (x / 256) % 256;
    int x3 = (x / 256 / 256) % 256;
    int x4 = x / 256 / 256 / 256;
    unsigned int res = x1 * 256;
    res += x2;
    res *= 256;
    res += x3;
    res *= 256;
    res += x4;
    return res;
}

void Md5Round(CV *cv_block, int *bin)
{
    unsigned int x[16];
    for (int i = 0; i < 16; i++)
    {
        x[i] = ToLittleEndian(bin + i * 32);
    }
    unsigned int a = cv_block->A;
    unsigned int b = cv_block->B;
    unsigned int c = cv_block->C;
    unsigned int d = cv_block->D;
    
    FF(a, b, c, d, x[0], 7, 0xd76aa478);
    FF(d, a, b, c, x[1], 12, 0xe8c7b756);
    FF(c, d, a, b, x[2], 17, 0x242070db);
    FF(b, c, d, a, x[3], 22, 0xc1bdceee);
    FF(a, b, c, d, x[4], 7, 0xf57c0faf);
    FF(d, a, b, c, x[5], 12, 0x4787c62a);
    FF(c, d, a, b, x[6], 17, 0xa8304613);
    FF(b, c, d, a, x[7], 22, 0xfd469501);
    FF(a, b, c, d, x[8], 7, 0x698098d8);
    FF(d, a, b, c, x[9], 12, 0x8b44f7af);
    FF(c, d, a, b, x[10], 17, 0xffff5bb1);
    FF(b, c, d, a, x[11], 22, 0x895cd7be);
    FF(a, b, c, d, x[12], 7, 0x6b901122);
    FF(d, a, b, c, x[13], 12, 0xfd987193);
    FF(c, d, a, b, x[14], 17, 0xa679438e);
    FF(b, c, d, a, x[15], 22, 0x49b40821);

    GG(a, b, c, d, x[1], 5, 0xf61e2562);
    GG(d, a, b, c, x[6], 9, 0xc040b340);
    GG(c, d, a, b, x[11], 14, 0x265e5a51);
    GG(b, c, d, a, x[0], 20, 0xe9b6c7aa);
    GG(a, b, c, d, x[5], 5, 0xd62f105d);
    GG(d, a, b, c, x[10], 9, 0x2441453);
    GG(c, d, a, b, x[15], 14, 0xd8a1e681);
    GG(b, c, d, a, x[4], 20, 0xe7d3fbc8);
    GG(a, b, c, d, x[9], 5, 0x21e1cde6);
    GG(d, a, b, c, x[14], 9, 0xc33707d6);
    GG(c, d, a, b, x[3], 14, 0xf4d50d87);
    GG(b, c, d, a, x[8], 20, 0x455a14ed);
    GG(a, b, c, d, x[13], 5, 0xa9e3e905);
    GG(d, a, b, c, x[2], 9, 0xfcefa3f8);
    GG(c, d, a, b, x[7], 14, 0x676f02d9);
    GG(b, c, d, a, x[12], 20, 0x8d2a4c8a);

    HH(a, b, c, d, x[5], 4, 0xfffa3942);
    HH(d, a, b, c, x[8], 11, 0x8771f681);
    HH(c, d, a, b, x[11], 16, 0x6d9d6122);
    HH(b, c, d, a, x[14], 23, 0xfde5380c);
    HH(a, b, c, d, x[1], 4, 0xa4beea44);
    HH(d, a, b, c, x[4], 11, 0x4bdecfa9);
    HH(c, d, a, b, x[7], 16, 0xf6bb4b60);
    HH(b, c, d, a, x[10], 23, 0xbebfbc70);
    HH(a, b, c, d, x[13], 4, 0x289b7ec6);
    HH(d, a, b, c, x[0], 11, 0xeaa127fa);
    HH(c, d, a, b, x[3], 16, 0xd4ef3085);
    HH(b, c, d, a, x[6], 23, 0x4881d05);
    HH(a, b, c, d, x[9], 4, 0xd9d4d039);
    HH(d, a, b, c, x[12], 11, 0xe6db99e5);
    HH(c, d, a, b, x[15], 16, 0x1fa27cf8);
    HH(b, c, d, a, x[2], 23, 0xc4ac5665);

    II(a, b, c, d, x[0], 6, 0xf4292244);
    II(d, a, b, c, x[7], 10, 0x432aff97);
    II(c, d, a, b, x[14], 15, 0xab9423a7);
    II(b, c, d, a, x[5], 21, 0xfc93a039);
    II(a, b, c, d, x[12], 6, 0x655b59c3);
    II(d, a, b, c, x[3], 10, 0x8f0ccc92);
    II(c, d, a, b, x[10], 15, 0xffeff47d);
    II(b, c, d, a, x[1], 21, 0x85845dd1);
    II(a, b, c, d, x[8], 6, 0x6fa87e4f);
    II(d, a, b, c, x[15], 10, 0xfe2ce6e0);
    II(c, d, a, b, x[6], 15, 0xa3014314);
    II(b, c, d, a, x[13], 21, 0x4e0811a1);
    II(a, b, c, d, x[4], 6, 0xf7537e82);
    II(d, a, b, c, x[11], 10, 0xbd3af235);
    II(c, d, a, b, x[2], 15, 0x2ad7d2bb);
    II(b, c, d, a, x[9], 21, 0xeb86d391);
    cv_block->A += a;
    cv_block->B += b;
    cv_block->C += c;
    cv_block->D += d;
}

void func_MD5(string cleartext, int *md5)
{
    str_to_bint(cleartext, md5);
    int len = cleartext.length() * 8;
    len = func_padding(md5, len, cleartext);
    CV cv_block;
    cv_block.A = 0x67452301;
    cv_block.B = 0xEFCDAB89;
    cv_block.C = 0x98BADCFE;
    cv_block.D = 0x10325476;
    for (int i = 0; i < len; i += 512)
    {
        Md5Round(&cv_block, md5 + i);
    }
    cv_block.A = IntToByte(cv_block.A);
    cv_block.B = IntToByte(cv_block.B);
    cv_block.C = IntToByte(cv_block.C);
    cv_block.D = IntToByte(cv_block.D);
    cout << "MD5密文:";
    cout.fill('0');
    cout.width(8);
    cout << hex << cv_block.A << cv_block.B << cv_block.C << cv_block.D << endl;
}

int main()
{
    string cleartext;
    int md5[1024] = {0};
    cout << "请输入明文:";
    getline(cin, cleartext);
    func_MD5(cleartext, md5);

    return 0;
}
