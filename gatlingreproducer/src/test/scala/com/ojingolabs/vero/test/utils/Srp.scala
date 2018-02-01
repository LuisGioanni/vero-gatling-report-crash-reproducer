package com.ojingolabs.vero.test.utils

import java.math.BigInteger
import java.security.SecureRandom

import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.agreement.srp.SRP6Util
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.util.BigIntegers
import org.bouncycastle.util.encoders.Hex
import java.nio.charset.StandardCharsets.UTF_8

object Srp {

  private val N: BigInteger = new BigInteger(
    "eeaf0ab9adb38dd69c33f80afa8fc5e86072618775ff3c0b9ea2314c9c256576d674df7496ea81d3383b4813d692c6e0e0d5d8e250b98be48e495c1d6089dad15dc7d7b46154d6b6ce8ef4ad69b15d4982559b297bcf1885c529f566660e57ec68edbc3c05726cc02fd4cbf4976eaa9afd5138fe8376435b9fc61d2fc0eb06e3",
    16)

  private val threadLocalSecureRandom: ThreadLocal[SecureRandom] = new ThreadLocal[SecureRandom] {
    override def initialValue(): SecureRandom =
      new SecureRandom
  }

  private val threadLocalDigest : ThreadLocal[Digest] = new ThreadLocal[Digest] {
    override def initialValue(): Digest =
      new SHA256Digest
  }

  private def withDigest[T](f: Digest => T): T = {
    val digest = threadLocalDigest.get()
    digest.reset()
    f(digest)
  }

  private val G: BigInteger = new BigInteger("02", 16)

  def generateClientPrivate(): BigInteger =
    withDigest(SRP6Util.generatePrivateValue(_, N, G, threadLocalSecureRandom.get()))

  def generateClientToken(clientPrivate: BigInteger): BigInteger =
    G.modPow(clientPrivate, N)

  def generateTokenString(token: BigInteger): String =
    new String(Hex.encode(BigIntegers.asUnsignedByteArray(token)), UTF_8)

  private def calculateX(digest: Digest, salt: Array[Byte], username: Array[Byte], password: Array[Byte]): BigInteger = {
    val output = new Array[Byte](digest.getDigestSize)

    digest.update(username, 0, username.length)
    digest.update(":".getBytes(UTF_8), 0, 1)
    digest.update(password, 0, password.length)
    digest.doFinal(output, 0)

    digest.update(salt, 0, salt.length)
    digest.update(output, 0, output.length)
    digest.doFinal(output, 0)

    new BigInteger(1, output)
  }

  private def calculateS(clientPrivate: BigInteger, clientToken: BigInteger, serverToken: BigInteger, login: String, password: String, salt: Array[Byte]): BigInteger = {
    val x = withDigest(calculateX(_, salt, login.getBytes(UTF_8), password.getBytes(UTF_8)))
    val B = SRP6Util.validatePublicValue(N, serverToken)
    val k = withDigest(SRP6Util.calculateK(_, N, G))
    val u = withDigest(SRP6Util.calculateU(_, N, clientToken, serverToken))

    val exp = u.multiply(x).add(clientPrivate)
    val tmp = G.modPow(x, N).multiply(k).mod(N)

    B.subtract(tmp).mod(N).modPow(exp, N)
  }

  def calculateKey(login: String, password: String, salt: Array[Byte], clientPrivate: BigInteger, clientToken: BigInteger,
    serverToken: Array[Byte]): String = {

    val B = BigIntegers.fromUnsignedByteArray(serverToken)
    val K = hash(calculateS(clientPrivate, clientToken, B, login, password, salt))
    val M = hash(hash(N).xor(hash(G)), hashS(login), BigIntegers.fromUnsignedByteArray(salt), clientToken, B, K).mod(N)

    new String(Hex.encode(BigIntegers.asUnsignedByteArray(M)))
  }

  private def hashS(values: String*): BigInteger =
    withDigest { digest =>
      values.foreach { arg =>
        val bytes = arg.getBytes(UTF_8)
        digest.update(bytes, 0, bytes.length)
      }

      val output = new Array[Byte](digest.getDigestSize)
      digest.doFinal(output, 0)

      new BigInteger(1, output)
    }

  private def hash(values: BigInteger*): BigInteger =
    withDigest { digest =>
      values.foreach { arg =>
        val bytes = BigIntegers.asUnsignedByteArray(arg)
        digest.update(bytes, 0, bytes.length)
      }

      val output = new Array[Byte](digest.getDigestSize)
      digest.doFinal(output, 0)

      new BigInteger(1, output)
  }
}