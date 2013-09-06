package molmed.qscripts

import org.testng.annotations._
import molmed.queue.SnpSeqBaseTest
import org.broadinstitute.sting.queue.pipeline._

/**
 * TODO
 * Implement cluster style testing as in AlignWithBwaSnpSeqPipelineTest
 */

class VariantCallingSnpSeqPipelineTest {

    val pathToScript = "-S src/main/scala/molmed/qscripts/VariantCalling.scala"

    val snpSeqBaseTest = SnpSeqBaseTest

    var run: Boolean = false

    @BeforeClass
    @Parameters(Array("runpipeline"))
    def init(runpipeline: Boolean): Unit = {
        this.run = runpipeline
    }

    @Test
    def testBasicVariantCalling {
        val projectName = "test1"
        val testRawSNV = projectName + ".raw.snv.vcf"
        val testRawINDEL = projectName + ".raw.indel.vcf"
        val spec = new PipelineTestSpec
        spec.jobRunners = Seq("Shell")
        spec.name = "VariantCallingPipeline"
        spec.args = Array(
            pathToScript,
            " -R " + snpSeqBaseTest.fullHumanGenome,
            " -res " + "/local/data/gatk_bundle/b37/",
            " -i " + snpSeqBaseTest.chromosome20Bam,
            " -intervals " + "/local/data/gatk_bundle/b37/first1000SNPsonChr20.intervals",
            //" -outputDir " + "target/pipelinetests/VariantCallingPipeline/Shell/run/",
            " --nbr_of_threads 1 ",
            " --scatter_gather 1 ",
            " -noRecal ",
            " --test_mode ",
            " -startFromScratch ",
            " -p " + projectName).mkString
        spec.fileMD5s += testRawSNV -> "5699ed88a0d2d97dfcacd6860f13e568"
        spec.fileMD5s += testRawINDEL -> "25c219248b0f1b850803dda08f393a18"
        PipelineTest.executeTest(spec, run)
    }

  //  /**
  //   * Note that this test will never be run (takes to long) but it will at least check that the 
  //   * script compiles without intervals being given.
  //   */
  @Test
  def testNoIntervalsVariantCalling {
    val projectName = "test1"
    val testRawSNV = projectName + ".raw.snv.vcf"
    val testRawINDEL = projectName + ".raw.indel.vcf"
    val spec = new PipelineTestSpec
    spec.jobRunners = Seq("Shell")
    spec.name = "VariantCallingPipeline"
    spec.args = Array(
      pathToScript,
      " -R " + snpSeqBaseTest.fullHumanGenome,
      " -res " + "/local/data/gatk_bundle/b37/",
      " -i " + snpSeqBaseTest.chromosome20Bam,
      " -outputDir " + "target/pipelinetests/VariantCallingPipeline/Shell/run/",
      " --nbr_of_threads 1 ",
      " --scatter_gather 1 ",
      " -noRecal ",
      " --test_mode ",
      " -startFromScratch ",
      " -p " + projectName).mkString
    PipelineTest.executeTest(spec, false)
  }
}
