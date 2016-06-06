package com.wix.pay.creditguard

import java.io.{StringReader, StringWriter}
import javax.xml.bind.{JAXBContext, Marshaller}

import com.wix.pay.creditguard.model.AshraitRequest

object RequestParser {
  def stringify(obj: AshraitRequest): String = {
    val context = JAXBContext.newInstance(classOf[AshraitRequest])
    val marshaller = context.createMarshaller
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true)

    val writer = new StringWriter
    try {
      marshaller.marshal(obj, writer)
    } finally {
      writer.close()
    }

    writer.toString
  }

  def parse(xml: String): AshraitRequest = {
    val context = JAXBContext.newInstance(classOf[AshraitRequest])
    val unmarshaller = context.createUnmarshaller

    val reader = new StringReader(xml)
    try {
      unmarshaller.unmarshal(reader).asInstanceOf[AshraitRequest]
    } finally {
      reader.close()
    }
  }
}
