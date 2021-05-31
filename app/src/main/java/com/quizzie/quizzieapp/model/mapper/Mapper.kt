package com.quizzie.quizzieapp.model.mapper

abstract class Mapper<Domain, Data> {

    abstract fun domainToData(domainModel: Domain): Data

    abstract fun dataToDomain(dataModel: Data): Domain

    fun domainToData(domainModel: List<Domain>): List<Data> = domainModel.map { domainToData(it) }
    fun dataToDomain(dataModel: List<Data>): List<Domain> = dataModel.map { dataToDomain(it) }
}