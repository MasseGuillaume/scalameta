package scala.meta

import scala.meta.internal.hosts.scalac.contexts.{Compiler => Compiler}
import scala.meta.internal.hosts.scalac.contexts.{Proxy => ProxyImpl}

object Mirror {
  def apply(artifacts: Artifact*)(implicit context: ArtifactContext): Mirror = {
    // TODO: In the future, we may avoid instantiating the entire compiler here,
    // because a mirror can theoretically be built on top of scala.reflect.runtime.universe
    // that supposedly starts up faster.
    new ProxyImpl(Compiler(), Domain(artifacts: _*)) {
      override def toString = s"Mirror(${artifacts.mkString(", ")})"
    }
  }
}

trait Mirror extends SemanticContext
