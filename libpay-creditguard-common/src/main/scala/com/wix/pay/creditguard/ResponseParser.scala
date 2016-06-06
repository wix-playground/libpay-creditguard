package com.wix.pay.creditguard

import java.io.{StringReader, StringWriter}
import javax.xml.bind.{JAXBContext, Marshaller}

import com.wix.pay.creditguard.model.AshraitResponse

object ResponseParser {
  def stringify(obj: AshraitResponse): String = {
    val context = JAXBContext.newInstance(classOf[AshraitResponse])
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

  def parse(xml: String): AshraitResponse = {
    val context = JAXBContext.newInstance(classOf[AshraitResponse])
    val unmarshaller = context.createUnmarshaller

    val reader = new StringReader(xml)
    try {
      unmarshaller.unmarshal(reader).asInstanceOf[AshraitResponse]
    } finally {
      reader.close()
    }
  }
}
