/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.kubernetes.client;

import io.fabric8.kubernetes.api.model.APIService;
import io.fabric8.kubernetes.api.model.APIServiceList;
import io.fabric8.kubernetes.api.model.Binding;
import io.fabric8.kubernetes.api.model.DeletionPropagation;
import io.fabric8.kubernetes.api.model.Doneable;
import io.fabric8.kubernetes.api.model.DoneableAPIService;
import io.fabric8.kubernetes.api.model.DoneableBinding;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesListBuilder;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.api.model.apiextensions.v1beta1.DoneableCustomResourceDefinition;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapList;
import io.fabric8.kubernetes.api.model.DoneableConfigMap;
import io.fabric8.kubernetes.api.model.ComponentStatus;
import io.fabric8.kubernetes.api.model.ComponentStatusList;
import io.fabric8.kubernetes.api.model.DoneableComponentStatus;
import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.EndpointsList;
import io.fabric8.kubernetes.api.model.DoneableEndpoints;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.EventList;
import io.fabric8.kubernetes.api.model.DoneableEvent;
import io.fabric8.kubernetes.api.model.LimitRange;
import io.fabric8.kubernetes.api.model.LimitRangeList;
import io.fabric8.kubernetes.api.model.DoneableLimitRange;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.DoneableNamespace;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.DoneableNode;
import io.fabric8.kubernetes.api.model.PersistentVolume;
import io.fabric8.kubernetes.api.model.PersistentVolumeList;
import io.fabric8.kubernetes.api.model.DoneablePersistentVolume;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimList;
import io.fabric8.kubernetes.api.model.DoneablePersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.DoneablePod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerList;
import io.fabric8.kubernetes.api.model.DoneableReplicationController;
import io.fabric8.kubernetes.api.model.ResourceQuota;
import io.fabric8.kubernetes.api.model.ResourceQuotaList;
import io.fabric8.kubernetes.api.model.DoneableResourceQuota;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretList;
import io.fabric8.kubernetes.api.model.DoneableSecret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.DoneableService;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.ServiceAccountList;
import io.fabric8.kubernetes.api.model.DoneableServiceAccount;
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequest;
import io.fabric8.kubernetes.api.model.certificates.CertificateSigningRequestList;
import io.fabric8.kubernetes.api.model.certificates.DoneableCertificateSigningRequest;
import io.fabric8.kubernetes.api.model.authentication.DoneableTokenReview;
import io.fabric8.kubernetes.api.model.authentication.TokenReview;
import io.fabric8.kubernetes.api.model.coordination.v1.DoneableLease;
import io.fabric8.kubernetes.api.model.coordination.v1.Lease;
import io.fabric8.kubernetes.api.model.coordination.v1.LeaseList;
import io.fabric8.kubernetes.client.dsl.*;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.dsl.internal.ClusterOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.CreateOnlyResourceOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.apiextensions.v1beta1.CustomResourceDefinitionOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.CustomResourceOperationContext;
import io.fabric8.kubernetes.client.dsl.internal.CustomResourceOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.KubernetesListOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.certificates.v1beta1.CertificateSigningRequestOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.APIServiceOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.ComponentStatusOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.NamespaceOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableImpl;
import io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.NodeOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.PersistentVolumeOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.PodOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.RawCustomResourceOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.ReplicationControllerOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.ServiceOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.coordination.v1.LeaseOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.BindingOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.ConfigMapOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.EndpointsOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.LimitRangeOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.PersistentVolumeClaimOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.ResourceQuotaOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.SecretOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.ServiceAccountOperationsImpl;
import io.fabric8.kubernetes.client.dsl.internal.core.v1.EventOperationsImpl;
import io.fabric8.kubernetes.client.extended.leaderelection.LeaderElectorBuilder;
import io.fabric8.kubernetes.client.extended.run.RunConfigBuilder;
import io.fabric8.kubernetes.client.extended.run.RunOperations;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import io.fabric8.kubernetes.client.utils.Utils;
import okhttp3.OkHttpClient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static io.fabric8.kubernetes.client.dsl.base.HasMetadataOperation.DEFAULT_PROPAGATION_POLICY;

/**
 * Class for Default Kubernetes Client implementing KubernetesClient interface.
 * It is thread safe.
 */
public class DefaultKubernetesClient extends BaseClient implements NamespacedKubernetesClient {

  public DefaultKubernetesClient() {
    super();
  }

  public DefaultKubernetesClient(String masterUrl) {
    super(masterUrl);
  }

  public DefaultKubernetesClient(Config config) {
    super(config);
  }


  public DefaultKubernetesClient(OkHttpClient httpClient, Config config) {
    super(httpClient, config);
  }

  public static DefaultKubernetesClient fromConfig(String config) {
    return new DefaultKubernetesClient(Serialization.unmarshal(config, Config.class));
  }

  public static DefaultKubernetesClient fromConfig(InputStream is) {
    return new DefaultKubernetesClient(Serialization.unmarshal(is, Config.class));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<ComponentStatus, ComponentStatusList, DoneableComponentStatus, Resource<ComponentStatus, DoneableComponentStatus>> componentstatuses() {
    return new ComponentStatusOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> load(InputStream is) {
    return new NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl(httpClient, getConfiguration(), getNamespace(), null, false, false, new ArrayList<>(), is, null, true, DeletionPropagation.BACKGROUND) {
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> resourceList(KubernetesResourceList item) {
    return new NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl(httpClient, getConfiguration(), getNamespace(), null, false, false, new ArrayList<>(), item, null, DeletionPropagation.BACKGROUND, true) {
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> resourceList(HasMetadata... items) {
    return resourceList(new KubernetesListBuilder().withItems(items).build());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> resourceList(Collection<HasMetadata> items) {
    return resourceList(new KubernetesListBuilder().withItems(new ArrayList<>(items)).build());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata, Boolean> resourceList(String s) {
    return new NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl(httpClient, getConfiguration(), getNamespace(), null, false, false, new ArrayList<>(), s, null, DeletionPropagation.BACKGROUND, true) {
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicable<HasMetadata, Boolean> resource(HasMetadata item) {
    return new NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableImpl(httpClient, getConfiguration(), getNamespace(), null, false, false, new ArrayList<>(), item, -1, DeletionPropagation.BACKGROUND, true, Waitable.DEFAULT_INITIAL_BACKOFF_MILLIS, Waitable.DEFAULT_BACKOFF_MULTIPLIER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicable<HasMetadata, Boolean> resource(String s) {
    return new NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableImpl(httpClient, getConfiguration(), getNamespace(), null, false, false, new ArrayList<>(), s, -1, DeletionPropagation.BACKGROUND, true, Waitable.DEFAULT_INITIAL_BACKOFF_MILLIS, Waitable.DEFAULT_BACKOFF_MULTIPLIER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<Binding, KubernetesResourceList<Binding>, DoneableBinding, Resource<Binding, DoneableBinding>> bindings() {
    return new BindingOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<Endpoints, EndpointsList, DoneableEndpoints, Resource<Endpoints, DoneableEndpoints>> endpoints() {
    return new EndpointsOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<Event, EventList, DoneableEvent, Resource<Event, DoneableEvent>> events() {
    return new EventOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NonNamespaceOperation<Namespace, NamespaceList, DoneableNamespace, Resource<Namespace, DoneableNamespace>> namespaces() {
    return new NamespaceOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NonNamespaceOperation<Node, NodeList, DoneableNode, Resource<Node, DoneableNode>> nodes() {
    return new NodeOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NonNamespaceOperation<PersistentVolume, PersistentVolumeList, DoneablePersistentVolume, Resource<PersistentVolume, DoneablePersistentVolume>> persistentVolumes() {
    return new PersistentVolumeOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<PersistentVolumeClaim, PersistentVolumeClaimList, DoneablePersistentVolumeClaim, Resource<PersistentVolumeClaim, DoneablePersistentVolumeClaim>> persistentVolumeClaims() {
    return new PersistentVolumeClaimOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>> pods() {
    return new PodOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<ReplicationController, ReplicationControllerList, DoneableReplicationController, RollableScalableResource<ReplicationController, DoneableReplicationController>> replicationControllers() {
    return new ReplicationControllerOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<ResourceQuota, ResourceQuotaList, DoneableResourceQuota, Resource<ResourceQuota, DoneableResourceQuota>> resourceQuotas() {
    return new ResourceQuotaOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SchedulingAPIGroupDSL scheduling() {
    return adapt(SchedulingAPIGroupClient.class);
  }

  @Override
  public MixedOperation<Secret, SecretList, DoneableSecret, Resource<Secret, DoneableSecret>> secrets() {
    return new SecretOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<Service, ServiceList, DoneableService, ServiceResource<Service, DoneableService>> services() {
    return new ServiceOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<ServiceAccount, ServiceAccountList, DoneableServiceAccount, Resource<ServiceAccount, DoneableServiceAccount>> serviceAccounts() {
    return new ServiceAccountOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<APIService, APIServiceList, DoneableAPIService, Resource<APIService, DoneableAPIService>> apiServices() {
    return new APIServiceOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public KubernetesListMixedOperation lists() {
    return new KubernetesListOperationsImpl(httpClient, getConfiguration(), getNamespace());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<ConfigMap, ConfigMapList, DoneableConfigMap, Resource<ConfigMap, DoneableConfigMap>> configMaps() {
    return new ConfigMapOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<LimitRange, LimitRangeList, DoneableLimitRange, Resource<LimitRange, DoneableLimitRange>> limitRanges() {
    return new LimitRangeOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NonNamespaceOperation<CustomResourceDefinition, CustomResourceDefinitionList, DoneableCustomResourceDefinition, Resource<CustomResourceDefinition, DoneableCustomResourceDefinition>> customResourceDefinitions() {
    return new CustomResourceDefinitionOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ApiextensionsAPIGroupDSL apiextensions() {
    return adapt(ApiextensionsAPIGroupClient.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NonNamespaceOperation<CertificateSigningRequest, CertificateSigningRequestList, DoneableCertificateSigningRequest, Resource<CertificateSigningRequest, DoneableCertificateSigningRequest>> certificateSigningRequests() {
    return new CertificateSigningRequestOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AuthorizationAPIGroupDSL authorization() {
    return adapt(AuthorizationAPIGroupClient.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Createable<TokenReview, TokenReview, DoneableTokenReview> tokenReviews() {
    return new CreateOnlyResourceOperationsImpl<>(httpClient, getConfiguration(), "authentication.k8s.io", "v1", Utils.getPluralFromKind(TokenReview.class.getSimpleName()), TokenReview.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends HasMetadata, L extends KubernetesResourceList<T>, D extends Doneable<T>> MixedOperation<T, L, D, Resource<T, D>> customResources(CustomResourceDefinitionContext crdContext, Class<T> resourceType, Class<L> listClass, Class<D> doneClass) {
    return new CustomResourceOperationsImpl<>(new CustomResourceOperationContext().withOkhttpClient(httpClient).withConfig(getConfiguration())
      .withCrdContext(crdContext)
      .withType(resourceType)
      .withListType(listClass)
      .withPropagationPolicy(DEFAULT_PROPAGATION_POLICY)
      .withDoneableType(doneClass));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends HasMetadata, L extends KubernetesResourceList<T>, D extends Doneable<T>> MixedOperation<T, L, D, Resource<T, D>> customResources(CustomResourceDefinition crd, Class<T> resourceType, Class<L> listClass, Class<D> doneClass) {
    return new CustomResourceOperationsImpl<>(new CustomResourceOperationContext().withOkhttpClient(httpClient).withConfig(getConfiguration())
      .withCrd(crd)
      .withType(resourceType)
      .withListType(listClass)
      .withPropagationPolicy(DEFAULT_PROPAGATION_POLICY)
      .withDoneableType(doneClass));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RawCustomResourceOperationsImpl customResource(CustomResourceDefinitionContext customResourceDefinition) {
    return new RawCustomResourceOperationsImpl(httpClient, getConfiguration(), customResourceDefinition);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends HasMetadata, L extends KubernetesResourceList<T>, D extends Doneable<T>> MixedOperation<T, L, D, Resource<T, D>> customResource(CustomResourceDefinition crd, Class<T> resourceType, Class<L> listClass, Class<D> doneClass) {
    return customResources(crd, resourceType, listClass, doneClass);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NamespacedKubernetesClient inNamespace(String namespace)
  {
    Config updated = new ConfigBuilder(getConfiguration()).withNamespace(namespace).build();
    return new DefaultKubernetesClient(httpClient, updated);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NamespacedKubernetesClient inAnyNamespace() {
    return inNamespace(null);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public FunctionCallable<NamespacedKubernetesClient> withRequestConfig(RequestConfig requestConfig) {
    return new WithRequestCallable<>(this, requestConfig);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtensionsAPIGroupDSL extensions() {
    return adapt(ExtensionsAPIGroupClient.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VersionInfo getVersion() {
    return new ClusterOperationsImpl(httpClient, getConfiguration(), ClusterOperationsImpl.KUBERNETES_VERSION_ENDPOINT).fetchVersion();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public V1APIGroupDSL v1() {
    return adapt(V1APIGroupClient.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AdmissionRegistrationAPIGroupDSL admissionRegistration() {
    return adapt(AdmissionRegistrationAPIGroupClient.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AppsAPIGroupDSL apps() {
    return adapt(AppsAPIGroupClient.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AutoscalingAPIGroupDSL autoscaling() {
    return adapt(AutoscalingAPIGroupClient.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkAPIGroupDSL network() { return adapt(NetworkAPIGroupClient.class); }

  /**
   * {@inheritDoc}
   */
  @Override
  public StorageAPIGroupDSL storage() { return adapt(StorageAPIGroupClient.class); }

  /**
   * {@inheritDoc}
   */
  @Override
  public BatchAPIGroupDSL batch() { return adapt(BatchAPIGroupClient.class); }

  /**
   * {@inheritDoc}
   */
  @Override
  public MetricAPIGroupDSL top() { return adapt(MetricAPIGroupClient.class); }

  /**
   * {@inheritDoc}
   */
  @Override
  public PolicyAPIGroupDSL policy() { return adapt(PolicyAPIGroupClient.class); }

  /**
   * {@inheritDoc}
   */
  @Override
  public RbacAPIGroupDSL rbac() { return adapt(RbacAPIGroupClient.class); }

  /**
   * {@inheritDoc}
   */
  @Override
  public SettingsAPIGroupDSL settings() { return adapt(SettingsAPIGroupClient.class); }

  /**
   * {@inheritDoc}
   */
  @Override
  public SharedInformerFactory informers() {
    return new SharedInformerFactory(ForkJoinPool.commonPool(), httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SharedInformerFactory informers(ExecutorService executorService) {
    return new SharedInformerFactory(executorService, httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LeaderElectorBuilder<NamespacedKubernetesClient> leaderElector() {
    return new LeaderElectorBuilder<>(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MixedOperation<Lease, LeaseList, DoneableLease, Resource<Lease, DoneableLease>> leases() {
    return new LeaseOperationsImpl(httpClient, getConfiguration());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RunOperations run() {
    return new RunOperations(httpClient, getConfiguration(), getNamespace(), new RunConfigBuilder());
  }
}
