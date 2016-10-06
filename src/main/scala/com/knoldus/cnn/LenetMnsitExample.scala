/*
package com.knoldus.cnn

/**
  * Created by shivansh on 29/7/16.
  */

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.conf.layers.{ConvolutionLayer, DenseLayer, OutputLayer, SubsamplingLayer}
import org.deeplearning4j.nn.conf.layers.setup.ConvolutionLayerSetup
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.slf4j.LoggerFactory

//import org.deeplearning4j.nn.conf.LearningRatePolicy;

/**
  * Created by agibsonccc on 9/16/15.
  */
object LenetMnsitExample extends App {
  val log = LoggerFactory.getLogger(this.getClass)

  val nChannels = 1
  val outputNum = 10
  val batchSize = 64
  val nEpochs = 10
  val iterations = 1
  val seed = 123

  log.info("Load data....")
  val mnistTrain = new MnistDataSetIterator(batchSize, true, 12345)
  val mnistTest = new MnistDataSetIterator(batchSize, false, 12345)

  log.info("Build model....")
  val builder = new NeuralNetConfiguration.Builder()
    .seed(seed)
    .iterations(iterations)
    .regularization(true).l2(0.0005)
    .learningRate(0.01) //.biasLearningRate(0.02)
    //.learningRateDecayPolicy(LearningRatePolicy.Inverse).lrPolicyDecayRate(0.001).lrPolicyPower(0.75)
    .weightInit(WeightInit.XAVIER)
    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
    .updater(Updater.NESTEROVS).momentum(0.9)
    .list()
    .layer(0, new ConvolutionLayer.Builder(5, 5)
      //nIn and nOut specify depth. nIn here is the nChannels and nOut is the number of filters to be applied
      .nIn(nChannels)
      .stride(1, 1)
      .nOut(20)
      .activation("identity")
      .build())
    .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
      .kernelSize(2, 2)
      .stride(2, 2)
      .build())
    .layer(2, new ConvolutionLayer.Builder(5, 5)
      //Note that nIn needed be specified in later layers
      .stride(1, 1)
      .nOut(50)
      .activation("identity")
      .build())
    .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
      .kernelSize(2, 2)
      .stride(2, 2)
      .build())
    .layer(4, new DenseLayer.Builder().activation("relu")
      .nOut(500).build())
    .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
      .nOut(outputNum)
      .activation("softmax")
      .build())
    .backprop(true).pretrain(false);
  // The builder needs the dimensions of the image along with the number of channels. these are 28x28 images in one channel
  new ConvolutionLayerSetup(builder, 28, 28, 1)

  val conf = builder.build()
  val model = new MultiLayerNetwork(conf)
  model.init();


  log.info("Train model....");
  model.setListeners(new ScoreIterationListener(1))
  (1 to nEpochs).toList.map { i =>
    model.fit(mnistTrain);
    log.info("*** Completed epoch {} ***", i)
    log.info("Evaluate model....")
    val eval = new Evaluation(outputNum)
    while (mnistTest.hasNext()) {
      val ds = mnistTest.next()
      val output = model.output(ds.getFeatureMatrix(), false)
      eval.eval(ds.getLabels(), output)
    }
    log.info(eval.stats())
    mnistTest.reset()
  }
  log.info("****************Example finished********************");

}*/
