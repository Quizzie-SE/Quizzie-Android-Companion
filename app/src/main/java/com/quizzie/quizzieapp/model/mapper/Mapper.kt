package com.quizzie.quizzieapp.model.mapper

interface Mapper <Domain, Data>{

    fun domainToData(domainModel: Domain): Data

    fun dataToDomain(dataModel: Data): Domain
}